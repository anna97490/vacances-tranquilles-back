package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;

@Data
public class SimpleUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String userRole;
    private String address;
    private String city;
    private String postalCode;
}

