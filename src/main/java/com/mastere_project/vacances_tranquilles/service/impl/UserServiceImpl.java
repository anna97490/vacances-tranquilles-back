package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.EmailAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.EmailNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.MissingFieldException;
import com.mastere_project.vacances_tranquilles.exception.WrongPasswordException;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.UserService;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service utilisateur gérant l'inscription, la connexion et
 * la sécurité des utilisateurs.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtConfig jwt;
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_MS = 10 * 60 * 1000; // 10 minutes
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedUntil = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Constructeur du service utilisateur.
     * 
     * @param userRepository  le repository utilisateur
     * @param passwordEncoder l'encodeur de mot de passe
     * @param userMapper      le mapper DTO entité utilisateur
     * @param jwtConfig       la configuration JWT
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper,
            JwtConfig jwtConfig) {
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
    public void registerClient(RegisterClientDTO dto) {
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
     * @throws MissingFieldException       si des champs obligatoires sont manquants
     * @throws EmailAlreadyExistsException si l'email existe déjà
     */
    @Override
    public void registerProvider(RegisterProviderDTO dto) {
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
     * @throws RuntimeException       en cas d'erreur inattendue ou de compte bloqué
     */
    @Override
    public LoginResponseDTO login(UserDTO userDTO) {
        String email = userDTO.getEmail();
        if (blockedUntil.containsKey(email) && blockedUntil.get(email) > System.currentTimeMillis()) {
            throw new RuntimeException("Trop de tentatives échouées. Réessayez plus tard.");
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

            String token = jwt.generateToken(user.getEmail(), user.getUserRole());
            return new LoginResponseDTO(token, user.getUserRole());

        } catch (EmailNotFoundException | WrongPasswordException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur serveur inattendue lors de la tentative de connexion", e);
        }
    }

    /**
     * Incrémente le nombre de tentatives de connexion pour un email et bloque le
     * compte si nécessaire.
     * 
     * @param email l'email de l'utilisateur
     */
    private void incrementLoginAttempts(String email) {
        int attempts = loginAttempts.getOrDefault(email, 0) + 1;
        if (attempts >= MAX_ATTEMPTS) {
            blockedUntil.put(email, System.currentTimeMillis() + BLOCK_TIME_MS);
            loginAttempts.remove(email);
            logger.warn("Email {} bloqué temporairement pour cause de brute force.", email);
        } else {
            loginAttempts.put(email, attempts);
        }
    }
}
