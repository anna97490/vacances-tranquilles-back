package com.mastere_project.vacances_tranquilles.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ConversationDTOTest {

    @Test
    void testGettersSettersEqualsHashCodeToString() {
        ConversationDTO c1 = new ConversationDTO();
        c1.setId(1L);
        c1.setUser1Id(2L);
        c1.setUser2Id(3L);
        c1.setCreatedAt(LocalDateTime.now());

        assertEquals(1L, c1.getId());
        assertEquals(2L, c1.getUser1Id());
        assertEquals(3L, c1.getUser2Id());
        
        ConversationDTO c2 = new ConversationDTO();
        c2.setId(1L);
        c2.setUser1Id(2L);
        c2.setUser2Id(3L);
        c2.setCreatedAt(c1.getCreatedAt());
        
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertTrue(c1.toString().contains("1"));
    }
} 