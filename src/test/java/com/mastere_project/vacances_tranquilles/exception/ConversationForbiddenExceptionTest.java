package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConversationForbiddenExceptionTest {
    
    @Test
    void testMessageAndInheritance() {
        String msg = "Forbidden";
        ConversationForbiddenException ex = new ConversationForbiddenException(msg);
        
        assertEquals(msg, ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
} 