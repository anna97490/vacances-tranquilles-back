package com.mastere_project.vacances_tranquilles.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Entité représentant une erreur à retourner à l'utilisateur via l'API.
 * Contient un code d'erreur et un message explicite.
 */
@Data
@AllArgsConstructor
public class ErrorEntity {
    private String code;
    private String message;
}
