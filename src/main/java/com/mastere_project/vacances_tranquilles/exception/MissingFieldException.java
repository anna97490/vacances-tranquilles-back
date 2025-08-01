package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un champ obligatoire est manquant lors d'une opération
 * métier.
 */
public class MissingFieldException extends RuntimeException {
    /**
     * Construit une nouvelle exception avec le message spécifié.
     * 
     * @param message le message d'erreur
     */
    public MissingFieldException(String message) {
        super(message);
    }
}
