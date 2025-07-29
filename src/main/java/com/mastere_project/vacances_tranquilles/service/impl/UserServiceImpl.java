package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.AccountLockedException;
import com.mastere_project.vacances_tranquilles.exception.EmailAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.EmailNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.LoginInternalException;
import com.mastere_project.vacances_tranquilles.exception.MissingFieldException;
import com.mastere_project.vacances_tranquilles.exception.WrongPasswordException;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.UserService;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service utilisateur gérant l'inscription, la connexion et la sécurité des utilisateurs.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtConfig jwt;
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_MS = 10 * 60 * 1000L; // 10 minutes
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedUntil = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Constructeur du service utilisateur.
     * 
     * @param userRepository le repository utilisateur
     * @param passwordEncoder l'encodeur de mot de passe
     * @param userMapper le mapper DTO entité utilisateur
     * @param jwtConfig la configuration JWT
     */
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder, 
            final UserMapper userMapper, final JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwt = jwtConfig;
    }

    /**
     * Inscrit un nouveau client.
     * 
     * @param dto les informations du client à enregistrer
     * @throws EmailAlreadyExistsException si l'email existe déjà
     */
    @Override
    public void registerClient(final RegisterClientDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Un compte avec cet email existe déjà.");
        }

        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Inscrit un nouveau prestataire.
     * 
     * @param dto les informations du prestataire à enregistrer
     * @throws MissingFieldException si des champs obligatoires sont manquants
     * @throws EmailAlreadyExistsException si l'email existe déjà
     */
    @Override
    public void registerProvider(final RegisterProviderDTO dto) {
        if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
            throw new MissingFieldException("Le nom de la société est obligatoire pour les prestataires.");
        }

        if (dto.getSiretSiren() == null || dto.getSiretSiren().isBlank()) {
            throw new MissingFieldException("Le numéro SIRET/SIREN est obligatoire pour les prestataires.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Un compte avec cet email existe déjà.");
        }

        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Authentifie un utilisateur (client ou prestataire).
     * 
     * @param userDTO les informations de connexion (email, mot de passe)
     * @return un objet LoginResponseDTO contenant le token JWT et le rôle
     * @throws EmailNotFoundException si l'email n'existe pas
     * @throws WrongPasswordException si le mot de passe est incorrect
     * @throws RuntimeException en cas d'erreur inattendue ou de compte bloqué
     */
    @Override
    public LoginResponseDTO login(final UserDTO userDTO) {
        String email = userDTO.getEmail();
        
        if (blockedUntil.containsKey(email) && blockedUntil.get(email) > System.currentTimeMillis()) {
            throw new AccountLockedException("Trop de tentatives échouées. Réessayez plus tard.");
        }

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                logger.warn("Tentative de connexion avec un email inconnu : {}", email);
                incrementLoginAttempts(email);
                throw new EmailNotFoundException("Aucun compte trouvé pour l'email : " + email);
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                logger.warn("Mot de passe incorrect pour l'email : {}", email);
                incrementLoginAttempts(email);
                throw new WrongPasswordException("Mot de passe incorrect pour l'email : " + email);
            }

            // Succès : reset des compteurs
            loginAttempts.remove(email);
            blockedUntil.remove(email);

            String token = jwt.generateToken(user.getId(), user.getUserRole());
            return new LoginResponseDTO(token, user.getUserRole());

        } catch (EmailNotFoundException | WrongPasswordException e) {
            throw e;
        } catch (Exception e) {
            throw new LoginInternalException(
                    "Erreur serveur inattendue lors de la tentative de connexion", e);
        }
    }

    /**
     * Récupère le profil de l'utilisateur authentifié.
     * 
     * @param userId l'identifiant de l'utilisateur authentifié
     * @return le profil de l'utilisateur
     * @throws AccessDeniedException si l'utilisateur n'est pas trouvé
     */
    @Override
    public UserProfileDTO getUserProfile(final Long userId) throws AccessDeniedException {
        return userRepository.findById(userId)
                .filter(user -> !user.getIsAnonymized())
                .map(userMapper::toUserProfileDTO)
                .orElseThrow(() -> new AccessDeniedException("Utilisateur non trouvé"));
    }

    /**
     * Met à jour le profil de l'utilisateur authentifié.
     * 
     * @param userId l'identifiant de l'utilisateur authentifié
     * @param updateDTO les données de mise à jour
     * @return le profil mis à jour
     * @throws AccessDeniedException si l'utilisateur n'est pas trouvé
     */
    @Override
    public UserProfileDTO updateUserProfile(final Long userId, final UpdateUserDTO updateDTO) {
        
        return userRepository.findById(userId)
                .filter(user -> !user.getIsAnonymized())
                .map(user -> {
                    user = userMapper.updateUserFromDTO(user, updateDTO);
                    User savedUser = userRepository.save(user);
                    
                    return userMapper.toUserProfileDTO(savedUser);
                })
                .orElseThrow(() -> new AccessDeniedException("Utilisateur non trouvé"));
    }

    /**
     * Supprime le compte de l'utilisateur authentifié (conformité RGPD).
     * 
     * @param userId l'identifiant de l'utilisateur authentifié
     * @throws AccessDeniedException si l'utilisateur n'est pas trouvé
     */
    @Override
    public void deleteUserAccount(final Long userId) {
        userRepository.findById(userId)
                .filter(user -> !user.getIsAnonymized())
                .ifPresentOrElse(user -> {
                    // Log de début de suppression pour conformité RGPD
                    logger.info("=== DÉBUT SUPPRESSION COMPTE RGPD ===");
                    logger.info("Utilisateur ID: {}", userId);
                    logger.info("Email: {}", user.getEmail());
                    logger.info("Rôle: {}", user.getUserRole());
                    logger.info("Raison de suppression: Demande utilisateur");
                    logger.info("Anonymisation obligatoire: true (conformité RGPD)");
                    logger.info("Timestamp: {}", LocalDateTime.now());
                    
                    // Anonymisation RGPD-compliant obligatoire
                    anonymizeUserData(user, "Demande utilisateur");
                    logger.info("Compte utilisateur anonymisé avec succès pour l'ID : {} - Raison : Demande utilisateur", 
                        userId);
                    
                    // Log de fin de suppression pour conformité RGPD
                    logger.info("=== FIN SUPPRESSION COMPTE RGPD ===");
                    logger.info("Action terminée avec succès");
                    logger.info("Timestamp: {}", LocalDateTime.now());
                }, () -> {
                    throw new AccessDeniedException("Utilisateur non trouvé");
                });
    }

    /**
     * Anonymise les données utilisateur conformément au RGPD.
     * 
     * @param user l'utilisateur à anonymiser
     * @param reason la raison de l'anonymisation
     */
    private void anonymizeUserData(final User user, final String reason) {
        // Log de début d'anonymisation
        logger.info("=== DÉBUT ANONYMISATION RGPD ===");
        logger.info("Anonymisation des données pour l'utilisateur ID: {}", user.getId());
        
        // Anonymisation des données personnelles
        user.setFirstName("ANONYME");
        user.setLastName("ANONYME");
        user.setEmail("anonyme_" + user.getId() + "@deleted.local");
        user.setPhoneNumber("0000000000");
        user.setAddress("ADRESSE SUPPRIMÉE");
        user.setCity("VILLE SUPPRIMÉE");
        user.setPostalCode("00000");
        user.setProfilePicture(null);
        
        // Anonymisation des données spécifiques aux prestataires
        if (user.getUserRole() == UserRole.PROVIDER) {
            user.setCompanyName("SOCIÉTÉ SUPPRIMÉE");
            user.setSiretSiren("00000000000000");
            logger.info("Données prestataire anonymisées");
        }
        
        // Marquer comme anonymisé et enregistrer les métadonnées RGPD
        user.setIsAnonymized(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setDeletionReason(reason != null ? reason : "Demande utilisateur");
        
        userRepository.save(user);
        
        // Log de fin d'anonymisation
        logger.info("Anonymisation terminée avec succès");
        logger.info("Données personnelles supprimées conformément au RGPD");
        logger.info("Métadonnées de suppression enregistrées");
        logger.info("=== FIN ANONYMISATION RGPD ===");
    }

    /**
     * Incrémente le nombre de tentatives de connexion pour un email et bloque le compte si nécessaire.
     * 
     * @param email l'email de l'utilisateur
     */
    private void incrementLoginAttempts(final String email) {
        int attempts = loginAttempts.getOrDefault(email, 0) + 1;
        
        if (attempts >= MAX_ATTEMPTS) {
            blockedUntil.put(email, System.currentTimeMillis() + BLOCK_TIME_MS);
            loginAttempts.remove(email);
            logger.warn("Email bloqué temporairement pour cause de brute force.");
        } else {
            loginAttempts.put(email, attempts);
        }
    }
}
