package com.mastere_project.vacances_tranquilles.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReservationStatusDTO {

    @NotNull(message = "Le statut est obligatoire")
    private ReservationStatus status;
    
    private String comments; // Commentaires optionnels sur le changement de statut
}
