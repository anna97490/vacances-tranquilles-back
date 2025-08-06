package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidTokenExceptionTest {

    @Test
    void constructor_WithMessage_ShouldCreateException() {
        String message = "Token JWT invalide ou expiré";

        InvalidTokenException exception = new InvalidTokenException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Token JWT invalide ou expiré";
        Throwable cause = new RuntimeException("Cause originale");

        InvalidTokenException exception = new InvalidTokenException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void exception_ShouldExtendRuntimeException() {
        String message = "Token JWT invalide ou expiré";

        InvalidTokenException exception = new InvalidTokenException(message);

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_WithNullMessage_ShouldCreateException() {
        InvalidTokenException exception = new InvalidTokenException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_WithNullCause_ShouldCreateException() {
        String message = "Token JWT invalide ou expiré";

        InvalidTokenException exception = new InvalidTokenException(message, null);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_WithEmptyMessage_ShouldCreateException() {
        String message = "";

        InvalidTokenException exception = new InvalidTokenException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }
} 