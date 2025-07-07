package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un email existe déjà lors de l'inscription d'un
 * utilisateur.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    /**
     * Construit une nouvelle exception avec le message spécifié.
     * 
     * @param message le message d'erreur
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
