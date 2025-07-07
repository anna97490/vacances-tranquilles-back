package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilisé pour l'inscription d'un nouveau client.
 */
@Data
public class RegisterClientDTO {
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @Email
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotBlank(message = "La ville est obligatoire")
    private String city;

    @NotBlank(message = "Le code postal est obligatoire")
    private String postalCode;
}
