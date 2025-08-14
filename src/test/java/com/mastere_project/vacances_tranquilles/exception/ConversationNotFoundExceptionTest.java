package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConversationNotFoundExceptionTest {
    
    @Test
    void testMessageAndInheritance() {
        String msg = "Not found";
        ConversationNotFoundException ex = new ConversationNotFoundException(msg);
        
        assertEquals(msg, ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
} 