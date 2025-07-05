package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une erreur inattendue survient lors de la tentative de connexion.
 */
public class UnexpectedLoginException extends RuntimeException {
    /**
     * Construit une nouvelle exception avec le message et la cause spécifiés.
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public UnexpectedLoginException(String message, Throwable cause) {
        super(message, cause);
    }
} 
