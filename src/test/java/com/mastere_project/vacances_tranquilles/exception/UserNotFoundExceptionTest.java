package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {
    
    @Test
    void testMessageAndInheritance() {
        String msg = "User not found";
        UserNotFoundException ex = new UserNotFoundException(msg);
        
        assertEquals(msg, ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
} 