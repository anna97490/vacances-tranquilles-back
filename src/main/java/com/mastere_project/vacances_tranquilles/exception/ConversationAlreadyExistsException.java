package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une conversation existe déjà entre deux utilisateurs.
 */
public class ConversationAlreadyExistsException extends RuntimeException {
    
    /**
     * Construit une nouvelle exception avec le message fourni.
     * @param message le détail de l'erreur
     */
    public ConversationAlreadyExistsException(String message) {
        super(message);
    }
} 