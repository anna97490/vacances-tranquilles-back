package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un statut de réservation invalide est fourni.
 */
public class InvalidReservationStatusException extends RuntimeException {
    
    /**
     * Constructeur avec message.
     * 
     * @param message Le message d'erreur
     */
    public InvalidReservationStatusException(String message) {
        super(message);
    }
    
    /**
     * Constructeur avec message et cause.
     * 
     * @param message Le message d'erreur
     * @param cause La cause de l'exception
     */
    public InvalidReservationStatusException(String message, Throwable cause) {
        super(message, cause);
    }
} 