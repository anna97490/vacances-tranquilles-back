package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour l'affichage des informations de base d'un utilisateur.
 * Contient le nom, prénom et les reviews reçues.
 */
@Data
@NoArgsConstructor
public class UserBasicInfoDTO {

    private String firstName;
    private String lastName;
    private List<ReviewDTO> reviews;
} 