package com.mastere_project.vacances_tranquilles.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    private Long id;
    private LocalDate reservationDate;
    private LocalTime startDate;
    private LocalTime endDate;
    private BigDecimal totalPrice;
    private ReservationStatus status;
    
    // Relations avec d'autres entités
    private Long clientId;
    private String clientName;
    private Long providerId;
    private String providerName;
    private Long serviceId;
    private String serviceName;
    private Long paymentId;
    
    // Champs additionnels pour l'affichage
    private String propertyName;
    private String comments;
    private String[] services;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

