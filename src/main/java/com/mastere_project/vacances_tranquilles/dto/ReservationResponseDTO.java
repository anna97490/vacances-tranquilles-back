package com.mastere_project.vacances_tranquilles.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long id;
    private LocalDateTime reservationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalPrice;
    private ReservationStatus status;
    
    // Informations sur les utilisateurs
    private Long clientId;
    private String clientName;
    private String clientEmail;
    private Long providerId;
    private String providerName;
    private String providerEmail;
    
    // Informations sur le service
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    
    // Informations sur le paiement
    private Long paymentId;
    private String paymentStatus;
    
    // Champs additionnels pour l'affichage
    private String propertyName;
    private String propertyAddress;
    private String comments;
    private List<String> services;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 