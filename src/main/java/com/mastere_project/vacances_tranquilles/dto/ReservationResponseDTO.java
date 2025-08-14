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
    
    // ID de la conversation si elle existe
    private Long conversationId;
} 