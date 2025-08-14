package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un email existe déjà dans le système.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    /**
     * Construit une nouvelle exception EmailAlreadyExistsException avec le message fourni.
     *
     * @param message le détail de l'erreur
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
