package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceDTO {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Double price;
    private String imageUrl;
    private Long providerId;
}
