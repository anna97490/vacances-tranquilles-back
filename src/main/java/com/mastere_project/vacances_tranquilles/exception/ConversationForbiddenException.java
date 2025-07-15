package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un utilisateur tente d'accéder à une conversation sans y être autorisé.
 */
public class ConversationForbiddenException extends RuntimeException {
    
    /**
     * Construit une nouvelle exception avec le message fourni.
     * @param message le détail de l'erreur
     */
    public ConversationForbiddenException(String message) {
        super(message);
    }
} 