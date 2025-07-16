package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Représente une réservation effectuée par un utilisateur.
 */
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    /** L'identifiant unique de la réservation. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // @ManyToOne
    // @JoinColumn(name = "customer_id")
    // private User customer;
    //
    // @ManyToOne
    // @JoinColumn(name = "provider_id")
    // private User provider;

    // @ManyToOne
    // @JoinColumn(name = "service_id")
    // private Service service;
    //
    // @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    // private Payment payment;
}