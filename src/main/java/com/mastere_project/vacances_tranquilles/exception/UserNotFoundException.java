package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un utilisateur n'est pas trouvé.
 */
public class UserNotFoundException extends RuntimeException {
    
    /**
     * Constructeur avec message.
     * 
     * @param message le message d'erreur
     */
    public UserNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructeur avec message et cause.
     * 
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 