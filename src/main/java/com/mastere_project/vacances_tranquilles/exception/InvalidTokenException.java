package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un token JWT est invalide, expiré ou manquant.
 */
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String message) {
        super(message);
    }
    
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
} 