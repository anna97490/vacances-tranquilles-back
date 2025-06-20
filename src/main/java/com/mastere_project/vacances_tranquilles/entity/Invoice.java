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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate;

    // Montants
    private Double amount;
    private String paymentMethod;

    // Liens vers les entités existantes
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User provider; 

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "schedule_id", unique = true)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
}