package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une réservation n'est pas trouvée.
 */
public class ReservationNotFoundException extends RuntimeException {
    
    public ReservationNotFoundException(String message) {
        super(message);
    }
    
    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 