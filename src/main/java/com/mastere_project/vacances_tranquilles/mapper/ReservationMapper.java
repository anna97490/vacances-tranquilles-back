package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;

/**
 * Interface du mapper pour la conversion entre entités Reservation et DTOs.
 * Définit les opérations de transformation des objets Reservation en ReservationDTO.
 */
public interface ReservationMapper {
    /**
     * Convertit une entité Reservation en ReservationDTO.
     * Mappe tous les champs de base et les objets associés (client, prestataire, service, paiement).
     * Gère les cas où les objets associés peuvent être null.
     *
     * @param reservation L'entité Reservation à convertir
     * @return Le ReservationDTO correspondant, ou null si reservation est null
     */
    ReservationDTO toDTO(Reservation reservation);
}
