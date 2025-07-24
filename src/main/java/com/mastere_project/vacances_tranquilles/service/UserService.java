package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.*;

/**
 * Service utilisateur : définit les opérations d'inscription et
 * d'authentification.
 */
public interface UserService {
    /**
     * Inscrit un nouveau client.
     * 
     * @param registerClientDTO les informations du client à enregistrer
     */
    void registerClient(RegisterClientDTO registerClientDTO);

    /**
     * Inscrit un nouveau prestataire.
     * 
     * @param registerProviderDTO les informations du prestataire à enregistrer
     */
    void registerProvider(RegisterProviderDTO registerProviderDTO);

    /**
     * Authentifie un utilisateur (client ou prestataire).
     * 
     * @param userDTO les informations de connexion (email, mot de passe)
     * @return un objet LoginResponseDTO contenant le token JWT et le rôle
     */
    LoginResponseDTO login(UserDTO userDTO);
}
