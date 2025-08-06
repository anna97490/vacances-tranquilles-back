package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedAccessExceptionTest {

    @Test
    void constructor_WithMessage_ShouldCreateException() {
        String message = "Accès non autorisé";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Accès non autorisé";
        Throwable cause = new RuntimeException("Cause originale");
        UnauthorizedAccessException exception = new UnauthorizedAccessException(message, cause);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void exception_ShouldExtendRuntimeException() {
        String message = "Accès non autorisé";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(message);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_WithNullCause_ShouldCreateException() {
        String message = "Accès non autorisé";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(message, null);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void exception_ShouldBeDifferentFromUserNotFoundException() {
        String message = "Accès non autorisé";
        UnauthorizedAccessException unauthorizedException = new UnauthorizedAccessException(message);
        UserNotFoundException userNotFoundException = new UserNotFoundException(message);
        assertNotEquals(unauthorizedException.getClass(), userNotFoundException.getClass());
        assertTrue(unauthorizedException instanceof RuntimeException);
        assertTrue(userNotFoundException instanceof RuntimeException);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Accès non autorisé : émojis 🚫 et caractères spéciaux @#$%"})
    void constructor_WithVariousMessages_ShouldCreateException(String message) {
        UnauthorizedAccessException exception = new UnauthorizedAccessException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_WithNullMessage_ShouldCreateException() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException(null);
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
} 