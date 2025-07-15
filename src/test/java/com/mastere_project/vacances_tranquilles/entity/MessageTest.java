package com.mastere_project.vacances_tranquilles.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        Message m1 = new Message();
        m1.setId(1L);
        m1.setContent("Hello");
        // Fixer la date pour éviter les différences de microsecondes
        java.time.LocalDateTime now = java.time.LocalDateTime.of(2023, 1, 1, 12, 0);
        m1.setSentAt(now);
        assertEquals(1L, m1.getId());
        assertEquals("Hello", m1.getContent());
        assertEquals(now, m1.getSentAt());

        Message m2 = new Message();
        m2.setId(1L);
        m2.setContent("Hello");
        m2.setSentAt(now);
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertTrue(m1.toString().contains("Hello"));
    }
} 