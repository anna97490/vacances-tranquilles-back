package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un utilisateur (client ou prestataire) de la plateforme.
 */
@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private String password;
    private UserRole userRole;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;

    private String siret;
    private String companyName;
}
