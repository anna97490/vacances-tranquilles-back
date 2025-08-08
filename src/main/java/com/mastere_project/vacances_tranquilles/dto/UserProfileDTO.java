package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour l'affichage du profil utilisateur.
 * Ne contient pas les informations sensibles comme le mot de passe.
 */
@Data
@NoArgsConstructor
public class UserProfileDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private String siretSiren;
    private String companyName;
} 