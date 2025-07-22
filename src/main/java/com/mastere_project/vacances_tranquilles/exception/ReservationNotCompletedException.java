package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une tentative d'accès ou de traitement d'une réservation
 * non finalisée est détectée.
 */
public class ReservationNotCompletedException extends RuntimeException {

    /**
     * Construit une nouvelle exception ReservationNotCompletedException avec le message spécifié.
     *
     * @param message le message détaillant la cause de l'exception
     */
    public ReservationNotCompletedException(String message) {
        super(message);
    }
}
