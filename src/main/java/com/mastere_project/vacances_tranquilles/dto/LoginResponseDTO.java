package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de réponse à la connexion, contenant le token JWT et le rôle utilisateur.
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {

    /** Le token JWT généré lors de la connexion. */
    private String token;
    
    /** Le rôle de l'utilisateur connecté. */
    private UserRole userRole;
}