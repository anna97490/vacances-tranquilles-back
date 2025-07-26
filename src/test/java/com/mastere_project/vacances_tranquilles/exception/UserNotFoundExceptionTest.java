package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    void constructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Utilisateur non trouvé";
        Throwable cause = new RuntimeException("Cause originale");

        UserNotFoundException exception = new UserNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void exception_ShouldExtendRuntimeException() {
        String message = "Utilisateur non trouvé";

        UserNotFoundException exception = new UserNotFoundException(message);

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_WithNullMessage_ShouldCreateException() {
        UserNotFoundException exception = new UserNotFoundException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_WithNullCause_ShouldCreateException() {
        String message = "Utilisateur non trouvé";

        UserNotFoundException exception = new UserNotFoundException(message, null);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
} 