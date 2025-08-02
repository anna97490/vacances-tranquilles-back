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

}