package com.mastere_project.vacances_tranquilles.entity.enums;

/**
 * Enumération des rôles utilisateurs possibles dans l'application.
 * Peut être utilisé pour la gestion des droits et des accès.
 */
public enum Role {
    /**
     * Rôle client : utilisateur consommateur de services.
     */
    CLIENT,
    /**
     * Rôle prestataire : utilisateur proposant des services.
     */
    PROVIDER,
    /**
     * Rôle administrateur : utilisateur ayant tous les droits.
     */
    ADMIN
}
