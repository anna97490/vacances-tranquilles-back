package com.mastere_project.vacances_tranquilles.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Représente une entité d'erreur pour les réponses d'exception.
 */
@Data
@AllArgsConstructor
public class ErrorEntity {
    private String code;
    private String message;
}
