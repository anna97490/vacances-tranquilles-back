package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour la réponse des réservations au frontend.
 * Ne contient que les informations essentielles à exposer.
 */
@Data
public class ReservationResponseDTO {
    private Long id;
    private ReservationStatus status; 
    private LocalDateTime reservationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalPrice;
} 