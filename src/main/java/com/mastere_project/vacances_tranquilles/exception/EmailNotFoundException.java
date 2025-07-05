package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'aucun utilisateur n'est trouvé pour un email donné.
 */
public class EmailNotFoundException extends RuntimeException {

    /**
     * Construit une nouvelle exception avec le message spécifié.
     * @param message le message d'erreur
     */
    public EmailNotFoundException(String message) {
        super(message);
    }
} 