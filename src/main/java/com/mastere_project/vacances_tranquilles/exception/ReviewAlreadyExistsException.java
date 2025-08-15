package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un avis existe déjà pour une réservation donnée.
 */
public class ReviewAlreadyExistsException extends RuntimeException {

    public ReviewAlreadyExistsException(String message) {
        super(message);
    }

    public ReviewAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
