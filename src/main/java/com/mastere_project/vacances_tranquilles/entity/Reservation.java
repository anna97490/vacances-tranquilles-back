package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationDate;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;
}
