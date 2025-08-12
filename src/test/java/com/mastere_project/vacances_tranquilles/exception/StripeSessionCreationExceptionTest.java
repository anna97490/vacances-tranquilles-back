package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StripeSessionCreationExceptionTest {

    @Test
    void testConstructorSetsMessage() {
        String message = "La création de la session Stripe a échoué";
        StripeSessionCreationException exception = new StripeSessionCreationException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "La création de la session Stripe a échoué";
        Throwable cause = new RuntimeException("Cause originale");
        StripeSessionCreationException exception = new StripeSessionCreationException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testIsInstanceOfRuntimeException() {
        StripeSessionCreationException exception = new StripeSessionCreationException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
}