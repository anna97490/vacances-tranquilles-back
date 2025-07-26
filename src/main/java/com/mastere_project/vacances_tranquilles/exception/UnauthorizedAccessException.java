package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un utilisateur tente d'accéder à une ressource sans autorisation.
 */
public class UnauthorizedAccessException extends RuntimeException {
    
    /**
     * Constructeur avec message.
     * 
     * @param message le message d'erreur
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    
    /**
     * Constructeur avec message et cause.
     * 
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
} 