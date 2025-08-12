package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une erreur survient lors des opérations Stripe
 * (création de session de paiement, récupération de session, etc.).
 */
public class StripeException extends RuntimeException {

    /**
     * Constructeur avec message.
     * 
     * @param message Le message d'erreur décrivant la raison de l'exception
     */
    public StripeException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     * 
     * @param message Le message d'erreur décrivant la raison de l'exception
     * @param cause   La cause de l'exception (généralement l'exception Stripe
     *                originale)
     */
    public StripeException(String message, Throwable cause) {
        super(message, cause);
    }
}