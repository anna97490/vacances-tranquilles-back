package com.mastere_project.vacances_tranquilles.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un service proposé par un prestataire.
 */
@Data
@NoArgsConstructor
public class ServiceDTO {

    private Long id;
    private String title;
    private String description;
    private String category;
    private BigDecimal price;
    private Long providerId;
}
