package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.entity.Message;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class MessageMapperImplTest {
    private final MessageMapperImpl mapper = new MessageMapperImpl();

    @Test
    void testToDto() {
        Message message = new Message();
        message.setId(1L);
        Conversation conv = new Conversation(); conv.setId(100L);
        User sender = new User(); sender.setId(200L);
        message.setConversation(conv);
        message.setSender(sender);
        message.setContent("Hello");
        LocalDateTime now = LocalDateTime.now();
        message.setSentAt(now);
        message.setRead(true);

        MessageDTO dto = mapper.toDto(message);
        
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getConversationId());
        assertEquals(200L, dto.getSenderId());
        assertEquals("Hello", dto.getContent());
        assertEquals(now, dto.getSentAt());
        assertTrue(dto.isRead());
    }

    @Test
    void testToEntity() {
        MessageDTO dto = new MessageDTO();
        dto.setId(2L);
        dto.setConversationId(101L);
        dto.setSenderId(201L);
        dto.setContent("World");
        LocalDateTime now = LocalDateTime.now();
        dto.setSentAt(now);
        dto.setRead(false);

        Message entity = mapper.toEntity(dto);
        
        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("World", entity.getContent());
        assertEquals(now, entity.getSentAt());
        assertFalse(entity.isRead());
        // Conversation et sender ne sont pas set dans le mapper
        assertNull(entity.getConversation());
        assertNull(entity.getSender());
    }

    @Test
    void testNullCases() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }
} 