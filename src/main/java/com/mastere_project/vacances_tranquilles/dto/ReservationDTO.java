package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO représentant une réservation effectuée par un client auprès d'un prestataire.
 */
@Data
public class ReservationDTO {

    private Long id;
    private ReservationStatus status; 
    private LocalDateTime reservationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalPrice;
    
    // Champs pour la création de réservation
    private Long clientId;
    private Long providerId;
    private Long serviceId;
    private Long paymentId;
}
