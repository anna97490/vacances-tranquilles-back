package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse de connexion (login) d'un utilisateur.
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private UserRole userRole;
}