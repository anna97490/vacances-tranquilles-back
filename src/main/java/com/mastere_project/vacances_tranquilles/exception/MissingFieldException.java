package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un champ requis est manquant.
 */
public class MissingFieldException extends RuntimeException {
    /**
     * Construit une nouvelle exception avec le message fourni.
     * @param message le détail de l'erreur
     */
    public MissingFieldException(String message) {
        super(message);
    }
}
