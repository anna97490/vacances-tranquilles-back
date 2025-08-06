package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une réservation est incomplète ou invalide.
 */
public class MissingReservationDataException extends RuntimeException {
    
    public MissingReservationDataException(String message) {
        super(message);
    }
}
