package com.mastere_project.vacances_tranquilles.exception;


/**
 * Exception levée lorsqu'un mot de passe incorrect est fourni lors de l'authentification.
 */
public class WrongPasswordException extends RuntimeException {

    /**
     * Construit une nouvelle exception WrongPasswordException avec le message spécifié.
     *
     * @param message le message d'erreur
     */
    public WrongPasswordException(String message) {
        super(message);
    }
} 
