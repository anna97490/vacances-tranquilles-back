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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private LocalDateTime reservationDate;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
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