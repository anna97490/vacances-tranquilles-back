package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour l'affichage des informations de base d'un utilisateur.
 * Ne contient que le nom et prénom pour la confidentialité.
 */
@Data
@NoArgsConstructor
public class UserBasicInfoDTO {

    private String firstName;
    private String lastName;
} 