package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;

@Data
public class SimpleServiceDTO {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long providerId;
}

