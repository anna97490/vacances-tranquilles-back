package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée en cas d'erreur interne inattendue lors de la connexion.
 */
public class LoginInternalException extends RuntimeException {
    /**
     * Construit une nouvelle LoginInternalException avec un message et une cause.
     * 
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public LoginInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}