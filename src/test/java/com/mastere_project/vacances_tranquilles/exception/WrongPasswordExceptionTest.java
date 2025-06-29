package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WrongPasswordExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Incorrect password";
        WrongPasswordException exception = new WrongPasswordException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        WrongPasswordException exception = new WrongPasswordException("msg");
        assertTrue(exception instanceof RuntimeException);
    }
}