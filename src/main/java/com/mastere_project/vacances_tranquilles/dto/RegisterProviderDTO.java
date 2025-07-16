package com.mastere_project.vacances_tranquilles.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO utilisé pour l'inscription d'un nouveau prestataire.
 */
@Data
public class RegisterProviderDTO {

    /** Le prénom du prestataire. */
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    /** Le nom de famille du prestataire. */
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    /** L'adresse email du prestataire. */
    @Email
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    /** Le mot de passe choisi par le prestataire. */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    /** Le numéro de téléphone du prestataire. */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;

    /** L'adresse postale du prestataire. */
    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    /** La ville de résidence du prestataire. */
    @NotBlank(message = "La ville est obligatoire")
    private String city;

    /** Le code postal de la ville du prestataire. */
    @NotBlank(message = "Le code postal est obligatoire")
    private String postalCode;

    /** Le nom de la société du prestataire (optionnel). */
    private String companyName;

    /** Le numéro SIRET ou SIREN de la société (optionnel). */
    private String siretSiren;
}
