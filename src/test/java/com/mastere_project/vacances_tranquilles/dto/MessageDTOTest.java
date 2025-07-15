package com.mastere_project.vacances_tranquilles.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class MessageDTOTest {
    
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        MessageDTO m1 = new MessageDTO();
        m1.setId(1L);
        m1.setConversationId(2L);
        m1.setSenderId(3L);
        m1.setContent("Hello");
        m1.setSentAt(LocalDateTime.now());
        m1.setRead(true);

        assertEquals(1L, m1.getId());
        assertEquals(2L, m1.getConversationId());
        assertEquals(3L, m1.getSenderId());
        assertEquals("Hello", m1.getContent());
        assertTrue(m1.isRead());

        MessageDTO m2 = new MessageDTO();
        m2.setId(1L);
        m2.setConversationId(2L);
        m2.setSenderId(3L);
        m2.setContent("Hello");
        m2.setSentAt(m1.getSentAt());
        m2.setRead(true);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertTrue(m1.toString().contains("Hello"));
    }
} 