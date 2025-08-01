package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;

/**
 * Interface du mapper pour la conversion entre entités Reservation et DTOs.
 * Définit les opérations de transformation des objets Reservation en ReservationDTO et ReservationResponseDTO.
 * Cette interface centralise la logique de mapping pour assurer la cohérence des conversions.
 */
public interface ReservationMapper {
    
    /**
     * Convertit une entité Reservation en ReservationDTO.
     * Mappe tous les champs de base et les objets associés (client, prestataire, service, paiement).
     * Gère les cas où les objets associés peuvent être null.
     * Cette méthode est utilisée pour les opérations de création et de modification de réservations.
     *
     * @param reservation L'entité Reservation à convertir
     * @return Le ReservationDTO correspondant, ou null si reservation est null
     * @throws IllegalArgumentException si les données de réservation sont invalides
     */
    ReservationDTO toDTO(Reservation reservation);

    /**
     * Convertit une entité Reservation en ReservationResponseDTO.
     * Mappe uniquement les champs de base : id, status, dates et prix.
     * Cette méthode est utilisée pour les réponses sécurisées vers le frontend,
     * excluant les informations sensibles des objets associés.
     *
     * @param reservation L'entité Reservation à convertir
     * @return Le ReservationResponseDTO correspondant, ou null si reservation est null
     * @throws IllegalArgumentException si les données de réservation sont invalides
     */
    ReservationResponseDTO toResponseDTO(Reservation reservation);
}
