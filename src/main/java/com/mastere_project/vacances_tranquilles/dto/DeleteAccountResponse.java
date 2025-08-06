package com.mastere_project.vacances_tranquilles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse de suppression de compte utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAccountResponse {
    
    /**
     * Message de confirmation de suppression.
     */
    private String message;
} 