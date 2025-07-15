package com.mastere_project.vacances_tranquilles.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Représente une conversation entre deux utilisateurs.
 */
class ConversationTest {
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        Conversation c1 = new Conversation();
        c1.setId(1L);
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 12, 0);
        c1.setCreatedAt(now);

        Conversation c2 = new Conversation();
        c2.setId(1L);
        c2.setCreatedAt(now);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertTrue(c1.toString().contains("Conversation"));
    }
} 