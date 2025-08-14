package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConversationAlreadyExistsExceptionTest {

    @Test
    void testMessageAndInheritance() {
        String msg = "Conversation already exists";
        ConversationAlreadyExistsException ex = new ConversationAlreadyExistsException(msg);
        
        assertEquals(msg, ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
} 