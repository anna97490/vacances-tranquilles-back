package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'une erreur survient lors de la création d'une session
 * de paiement Stripe.
 */
public class StripeSessionCreationException extends RuntimeException {

    /**
     * Constructeur avec message.
     * 
     * @param message Le message d'erreur décrivant la raison de l'exception
     */
    public StripeSessionCreationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     * 
     * @param message Le message d'erreur décrivant la raison de l'exception
     * @param cause   La cause de l'exception (généralement l'exception Stripe
     *                originale)
     */
    public StripeSessionCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}