package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une réservation est introuvable en base de données.
 * Cette exception est généralement levée lors de tentatives d'accès à une réservation
 * qui n'existe pas ou qui n'est pas accessible par l'utilisateur authentifié.
 */
public class ReservationNotFoundException extends RuntimeException {
    
    /**
     * Constructeur avec message d'erreur personnalisé.
     * 
     * @param message Le message d'erreur décrivant la raison de l'exception
     */
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
