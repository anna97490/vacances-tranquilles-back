package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une réservation est introuvable.
 */
public class ReservationNotFoundException extends RuntimeException {
    
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
