package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailNotFoundExceptionTest {

    @Test
    void testConstructorSetsMessage() {
        String message = "Email not found";
        EmailNotFoundException exception = new EmailNotFoundException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testIsInstanceOfRuntimeException() {
        EmailNotFoundException exception = new EmailNotFoundException("Test");
        
        assertTrue(exception instanceof RuntimeException);
    }
}