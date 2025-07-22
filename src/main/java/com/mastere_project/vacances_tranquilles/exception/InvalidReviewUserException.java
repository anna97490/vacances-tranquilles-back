package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un utilisateur tente d'effectuer une action invalide liée aux avis.
 * Par exemple, lorsqu'un utilisateur essaie de s’auto-évaluer ou enfreint les règles de notation.
 */
public class InvalidReviewUserException extends RuntimeException {

    /**
     * Construit une nouvelle exception InvalidReviewUserException avec le message spécifié.
     *
     * @param message le message détaillant la cause de l'exception
     */
    public InvalidReviewUserException(String message) {
        super(message);
    }
}
