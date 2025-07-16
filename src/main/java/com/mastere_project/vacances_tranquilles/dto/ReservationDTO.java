package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO représentant une réservation effectuée par un client auprès d'un prestataire.
 */
@Data
public class ReservationDTO {

    /** L'identifiant unique de la réservation. */
    private Long id;
    
    /** Le statut actuel de la réservation (ex : en attente, confirmée, annulée). */
    private String status;
    
    /** La date et l'heure à laquelle la réservation a été effectuée. */
    private LocalDateTime reservationDate;
    
    /** Le commentaire éventuel laissé par le client lors de la réservation. */
    private String comment;
    
    /** La date et l'heure de début de la réservation. */
    private LocalDateTime startDate;
    
    /** La date et l'heure de fin de la réservation. */
    private LocalDateTime endDate;
    
    /** Le prix total de la réservation. */
    private Double totalPrice;
    
    /** L'identifiant du client ayant effectué la réservation. */
    private Long customerId;
    
    /** L'identifiant du prestataire auprès duquel la réservation a été faite. */
    private Long providerId;
    
    /** L'identifiant du service réservé. */
    private Long serviceId;
}
