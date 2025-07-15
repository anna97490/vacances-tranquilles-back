package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un utilisateur demandé n'est pas trouvé dans le système.
 */
public class UserNotFoundException extends RuntimeException {
    
    /**
     * Construit une nouvelle exception avec le message fourni.
     * @param message le détail de l'erreur
     */
    public UserNotFoundException(String message) {
        super(message);
    }
} 