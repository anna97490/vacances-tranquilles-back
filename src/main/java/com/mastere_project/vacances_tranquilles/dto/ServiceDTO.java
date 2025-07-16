package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un service proposé par un prestataire.
 */
@Data
@NoArgsConstructor
public class ServiceDTO {

    /** L'identifiant unique du service. */
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
    
    /** L'identifiant du prestataire proposant le service. */
    private Long providerId;
}
