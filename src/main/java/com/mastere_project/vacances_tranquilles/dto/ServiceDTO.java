package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceDTO {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private boolean isAvailable;
    private String postalCode;

    private Long providerId;      // ID du prestataire
    private String providerName;  // Nom complet du prestataire (optionnel mais pratique)
}
