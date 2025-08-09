package com.mastere_project.vacances_tranquilles.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReservationDTO {

    @NotNull(message = "La date de réservation est obligatoire")
    private LocalDate reservationDate;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime startDate;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime endDate;

    @NotNull(message = "Le prix total est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal totalPrice;

    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;

    @NotNull(message = "L'ID du prestataire est obligatoire")
    private Long providerId;

    @NotNull(message = "L'ID du service est obligatoire")
    private Long serviceId;

    private Long paymentId;

    // Champs optionnels
    private String propertyName;
    private String comments;
    private List<String> services;
}

