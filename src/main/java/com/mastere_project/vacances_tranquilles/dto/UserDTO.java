package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private String role;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;

    private String siret;
    private String companyName;
}

