package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO représentant une réservation effectuée par un client auprès d'un
 * prestataire.
 */
@Data
public class ReservationDTO {
    private Long id;
    private String status;
    private LocalDateTime reservationDate;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    private Long customerId;
    private Long providerId;
    private Long serviceId;
}
