package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'aucune réservation correspondante n'a été trouvée en base de données.
 * 
 * Cela peut indiquer que l'ID fourni est incorrect ou que la réservation a été supprimée.
 */
public class ReservationNotFoundException extends RuntimeException {

    /**
     * Construit une nouvelle exception ReservationNotFoundException avec le message spécifié.
     *
     * @param message le message décrivant la cause de l'exception
     */
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
