package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StripeExceptionTest {

    @Test
    void testConstructorSetsMessage() {
        String message = "Erreur lors de l'opération Stripe";
        StripeException exception = new StripeException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Erreur lors de l'opération Stripe";
        Throwable cause = new RuntimeException("Cause originale");
        StripeException exception = new StripeException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testIsInstanceOfRuntimeException() {
        StripeException exception = new StripeException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
}