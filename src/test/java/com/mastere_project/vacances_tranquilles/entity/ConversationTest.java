package com.mastere_project.vacances_tranquilles.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConversationTest {
    
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        Conversation c1 = new Conversation();
        c1.setId(1L);
        assertEquals(1L, c1.getId());

        Conversation c2 = new Conversation();
        c2.setId(1L);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertTrue(c1.toString().contains("Conversation"));
    }
} 