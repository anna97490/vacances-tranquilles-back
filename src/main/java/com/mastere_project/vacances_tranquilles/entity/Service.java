package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un service proposé par un prestataire.
 */
@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    /** L'identifiant unique du service. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Le titre du service proposé. */
    private String title;
    
    /** La description détaillée du service. */
    private String description;
    
    /** La catégorie à laquelle appartient le service. */
    private String category;
    
    /** Le prix du service. */
    private Double price;
    
    /** L'URL de l'image illustrant le service. */
    private String imageUrl;
}