package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un utilisateur tente d'accéder à une réservation
 * pour laquelle il n'a pas les autorisations nécessaires.
 * 
 */
public class UnauthorizedReservationAccessException extends RuntimeException {
    
    /**
     * Constructeur avec message.
     * 
     * @param message Le message d'erreur
     */
    public UnauthorizedReservationAccessException(String message) {
        super(message);
    }
    
    /**
     * Constructeur avec message et cause.
     * 
     * @param message Le message d'erreur
     * @param cause La cause de l'exception
     */
    public UnauthorizedReservationAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
