package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ConversationMapperImplTest {
    private final ConversationMapperImpl mapper = new ConversationMapperImpl();

    @Test
    void testToDto() {
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        User user1 = new User(); user1.setId(10L);
        User user2 = new User(); user2.setId(20L);
        conversation.setUser1(user1);
        conversation.setUser2(user2);
        LocalDateTime now = LocalDateTime.now();
        conversation.setCreatedAt(now);

        ConversationDTO dto = mapper.toDto(conversation);
       
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUser1Id());
        assertEquals(20L, dto.getUser2Id());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testToEntity() {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(2L);
        dto.setUser1Id(11L);
        dto.setUser2Id(22L);
        LocalDateTime now = LocalDateTime.now();
        dto.setCreatedAt(now);

        Conversation entity = mapper.toEntity(dto);
        
        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals(now, entity.getCreatedAt());
        // Les users ne sont pas set dans le mapper
        assertNull(entity.getUser1());
        assertNull(entity.getUser2());
    }

    @Test
    void testNullCases() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }
} 