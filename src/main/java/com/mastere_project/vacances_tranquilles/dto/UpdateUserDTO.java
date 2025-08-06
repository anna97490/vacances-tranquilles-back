package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la mise à jour du profil utilisateur.
 * Tous les champs sont optionnels pour permettre une mise à jour partielle.
 */
@Data
@NoArgsConstructor
public class UpdateUserDTO {

    private String firstName;
    private String lastName;
    private String profilePicture;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private String siretSiren;
    private String companyName;
} 