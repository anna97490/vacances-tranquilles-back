package com.mastere_project.vacances_tranquilles.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private Date paymentDate;
    private String method; // Stripe, virement, etc.

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Payeur (particulier)

    @OneToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;  // Lien vers la prestation réservée
}

