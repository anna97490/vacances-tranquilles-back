package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la mise à jour du statut d'une réservation.
 * Utilisé dans l'endpoint PATCH /api/reservations/{id}/status pour permettre
 * aux prestataires de modifier le statut de leurs réservations.
 */
@Data
@NoArgsConstructor
public class UpdateReservationStatusDTO {

    /**
     * Le nouveau statut à appliquer à la réservation.
     * Ne peut pas être null et doit respecter les règles de transition.
     */
    @NotNull
    private ReservationStatus status;

}
