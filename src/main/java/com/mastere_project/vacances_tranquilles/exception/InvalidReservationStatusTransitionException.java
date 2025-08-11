package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une transition de statut de réservation invalide est tentée.
 * 
 * @author Mastere Project Team
 * @version 1.0
 */
public class InvalidReservationStatusTransitionException extends RuntimeException {
    
    /**
     * Constructeur avec message.
     * 
     * @param message Le message d'erreur
     */
    public InvalidReservationStatusTransitionException(String message) {
        super(message);
    }
    
    /**
     * Constructeur avec message et cause.
     * 
     * @param message Le message d'erreur
     * @param cause La cause de l'exception
     */
    public InvalidReservationStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
} 