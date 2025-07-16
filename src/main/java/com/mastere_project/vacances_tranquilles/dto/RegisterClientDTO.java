package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour l'inscription d'un client.
 */
@Data
public class RegisterClientDTO {
    /** Le prénom du client. */
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    /** Le nom de famille du client. */
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    /** L'adresse email du client. */
    @Email
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    /** Le mot de passe choisi par le client. */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    /** Le numéro de téléphone du client. */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;

    /** L'adresse postale du client. */
    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    /** La ville de résidence du client. */
    @NotBlank(message = "La ville est obligatoire")
    private String city;

    /** Le code postal de la ville du client. */
    @NotBlank(message = "Le code postal est obligatoire")
    private String postalCode;
}
