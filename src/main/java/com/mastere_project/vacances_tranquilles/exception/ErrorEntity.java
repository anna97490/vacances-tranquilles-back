package com.mastere_project.vacances_tranquilles.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Représente une entité d'erreur pour les réponses d'exception.
 */
@Data
@AllArgsConstructor
public class ErrorEntity {
    /** Le code d'erreur associé à l'exception. */
    private String code;
    /** Le message détaillant l'erreur. */
    private String message;
}
