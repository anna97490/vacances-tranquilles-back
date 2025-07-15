package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une conversation demandée n'est pas trouvée.
 */
public class ConversationNotFoundException extends RuntimeException {
    
    /**
     * Construit une nouvelle exception avec le message fourni.
     * @param message le détail de l'erreur
     */
    public ConversationNotFoundException(String message) {
        super(message);
    }
} 