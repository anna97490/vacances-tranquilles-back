package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginInternalExceptionTest {
    @Test
    void testMessageAndCause() {
        String message = "Erreur interne";
        Throwable cause = new RuntimeException("Cause initiale");
        LoginInternalException exception = new LoginInternalException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}