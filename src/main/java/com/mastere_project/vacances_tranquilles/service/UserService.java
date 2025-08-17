package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.*;
import org.springframework.security.access.AccessDeniedException;

/**
 * Interface du service de gestion des utilisateurs.
 * Définit les opérations d'inscription, de connexion et de gestion des profils utilisateurs.
 */
public interface UserService {

    /**
     * Inscrit un nouveau client.
     * 
     * @param dto les informations du client à enregistrer
     */
    void registerClient(RegisterClientDTO dto);

    /**
     * Inscrit un nouveau prestataire.
     * 
     * @param dto les informations du prestataire à enregistrer
     */
    void registerProvider(RegisterProviderDTO dto);

    /**
     * Authentifie un utilisateur (client ou prestataire).
     * 
     * @param userDTO les informations de connexion (email, mot de passe)
     * @return un objet LoginResponseDTO contenant le token JWT et le rôle
     */
    LoginResponseDTO login(UserDTO userDTO);

    /**
     * Récupère le profil de l'utilisateur authentifié.
     * 
     * @return le profil de l'utilisateur
     * @throws AccessDeniedException si l'utilisateur n'est pas trouvé
     */
    UserProfileDTO getUserProfile() throws AccessDeniedException;

    /**
     * Met à jour le profil de l'utilisateur authentifié.
     * 
     * @param updateDTO les données de mise à jour
     * @return le profil mis à jour
     * @throws AccessDeniedException si l'utilisateur n'est pas trouvé
     */
    UserProfileDTO updateUserProfile(UpdateUserDTO updateDTO) throws AccessDeniedException;

    /**
     * Supprime le compte de l'utilisateur authentifié (conformité RGPD).
     * 
     * @throws AccessDeniedException si l'utilisateur n'est pas trouvé
     */
    void deleteUserAccount() throws AccessDeniedException;

    /**
     * Récupère les informations de base d'un utilisateur par son ID.
     * Ne retourne que le nom et prénom pour la confidentialité.
     * 
     * @param userId l'identifiant de l'utilisateur à récupérer
     * @return les informations de base de l'utilisateur (nom et prénom)
     * @throws AccessDeniedException si l'utilisateur n'est pas trouvé ou si l'accès est refusé
     */
    UserBasicInfoDTO getUserBasicInfoById(Long userId) throws AccessDeniedException;
}