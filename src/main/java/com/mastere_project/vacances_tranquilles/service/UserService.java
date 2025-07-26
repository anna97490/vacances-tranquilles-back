package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.*;
import java.util.List;

/**
 * Service utilisateur : définit les opérations d'inscription et d'authentification.
 */
public interface UserService {
    /**
     * Inscrit un nouveau client.
     * @param registerClientDTO les informations du client à enregistrer
     */
    void registerClient(RegisterClientDTO registerClientDTO);

    
    /**
     * Inscrit un nouveau prestataire.
     * @param registerProviderDTO les informations du prestataire à enregistrer
     */
    void registerProvider(RegisterProviderDTO registerProviderDTO);


    /**
     * Authentifie un utilisateur (client ou prestataire).
     * @param userDTO les informations de connexion (email, mot de passe)
     * @return un objet LoginResponseDTO contenant le token JWT et le rôle
     */
    LoginResponseDTO login(UserDTO userDTO);

    /**
     * Récupère tous les clients.
     * @return la liste des clients
     */
    List<UserProfileDTO> getAllClients();

    /**
     * Récupère tous les prestataires.
     * @return la liste des prestataires
     */
    List<UserProfileDTO> getAllProviders();

    /**
     * Récupère un client par son ID.
     * @param id l'identifiant du client
     * @return le profil du client
     */
    UserProfileDTO getClientById(Long id);

    /**
     * Récupère un prestataire par son ID.
     * @param id l'identifiant du prestataire
     * @return le profil du prestataire
     */
    UserProfileDTO getProviderById(Long id);

    /**
     * Récupère le profil de l'utilisateur authentifié.
     * @param userId l'identifiant de l'utilisateur authentifié
     * @return le profil de l'utilisateur
     */
    UserProfileDTO getUserProfile(Long userId);

    /**
     * Met à jour le profil de l'utilisateur authentifié.
     * @param userId l'identifiant de l'utilisateur authentifié
     * @param updateDTO les données de mise à jour
     * @return le profil mis à jour
     */
    UserProfileDTO updateUserProfile(Long userId, UpdateUserDTO updateDTO);

    /**
     * Supprime le compte de l'utilisateur authentifié (conformité RGPD).
     * @param userId l'identifiant de l'utilisateur authentifié
     * @param deleteAccountDTO les informations de suppression
     */
    void deleteUserAccount(Long userId, DeleteAccountDTO deleteAccountDTO);
} 
