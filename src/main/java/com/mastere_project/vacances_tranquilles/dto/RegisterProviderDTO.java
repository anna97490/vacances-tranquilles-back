package com.mastere_project.vacances_tranquilles.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterProviderDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String companyName;

    @NotBlank
    private String siretSiren;

    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
}
