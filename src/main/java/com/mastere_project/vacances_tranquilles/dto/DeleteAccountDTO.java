package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la suppression de compte utilisateur.
 * Conformité RGPD : anonymisation obligatoire des données personnelles.
 */
@Data
@NoArgsConstructor
public class DeleteAccountDTO {
    /**
     * Raison de la suppression du compte utilisateur.
     * Utilisé pour la traçabilité RGPD.
     */
    private String reason;
} 