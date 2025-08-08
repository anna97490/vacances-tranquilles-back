package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service pour la gestion des services proposés par les prestataires.
 */
public interface ServiceService {
    /**
     * Crée un nouveau service.
     * 
     * @param serviceDTO les informations du service à créer
     * @return le service créé
     */
    ServiceDTO createService(ServiceDTO serviceDTO);

    /**
     * Supprime un service par son identifiant.
     * 
     * @param id identifiant du service à supprimer
     */
    void deleteService(Long id);

    /**
     * Récupère un service par son identifiant.
     * 
     * @param id identifiant du service
     * @return le service correspondant
     */
    ServiceDTO getServiceById(Long id);

    /**
     * Récupère tous les services du prestataire connecté.
     * 
     * @return liste des services du prestataire connecté
     */
    List<ServiceDTO> getMyServices();

    /**
     * Met à jour partiellement un service existant. Seuls les champs non nuls du
     * DTO sont modifiés.
     * 
     * @param id         identifiant du service à modifier
     * @param serviceDTO DTO contenant les champs à mettre à jour
     * @return le ServiceDTO mis à jour
     */
    ServiceDTO partialUpdateService(Long id, ServiceDTO serviceDTO);

    /**
     * Recherche les services disponibles selon les critères et la disponibilité
     * réelle des prestataires.
     *
     * @param category   Catégorie du service (obligatoire)
     * @param postalCode Code postal du prestataire (obligatoire)
     * @param date       Date souhaitée (obligatoire)
     * @param startTime  Heure de début souhaitée (obligatoire)
     * @param endTime    Heure de fin souhaitée (obligatoire)
     * @return Liste des services disponibles
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    List<ServiceDTO> searchAvailableServices(
            String category,
            String postalCode,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime);
}