package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.Message;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException;
import com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.MessageMapper;
import com.mastere_project.vacances_tranquilles.repository.ConversationRepository;
import com.mastere_project.vacances_tranquilles.repository.MessageRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    @Mock MessageRepository messageRepository;

    @Mock MessageMapper messageMapper;
    
    @Mock ConversationRepository conversationRepository;

    @Mock UserRepository userRepository;

    @InjectMocks MessageServiceImpl service;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGetMessagesByConversationIdMarksRead() {
        Message m1 = new Message(); m1.setId(1L); User sender = new User(); sender.setId(2L); m1.setSender(sender); m1.setRead(false);
        Message m2 = new Message(); m2.setId(2L); User sender2 = new User(); sender2.setId(1L); m2.setSender(sender2); m2.setRead(false);
        when(messageRepository.findByConversationIdOrderBySentAtAsc(10L)).thenReturn(List.of(m1, m2));
        when(messageMapper.toDto(any())).thenReturn(new MessageDTO());
        when(messageRepository.saveAll(any())).thenReturn(List.of(m1, m2));
        List<MessageDTO> result = service.getMessagesByConversationId(10L, 1L);
        
        assertEquals(2, result.size());
        assertTrue(m1.isRead());
    }

    @Test
    void testGetAllMessages() {
        Message m = new Message(); m.setId(1L);
        when(messageRepository.findAll()).thenReturn(List.of(m));
        MessageDTO dto = new MessageDTO(); dto.setId(1L);
        when(messageMapper.toDto(m)).thenReturn(dto);
        List<MessageDTO> result = service.getAllMessages();
        
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testSendMessageSuccess() {
        MessageDTO dto = new MessageDTO(); dto.setConversationId(10L); dto.setSenderId(2L); dto.setContent("msg");
        Message entity = new Message(); entity.setId(1L); entity.setContent("msg");
        Conversation conv = new Conversation(); conv.setId(10L);
        User u1 = new User(); u1.setId(2L);
        User u2 = new User(); u2.setId(3L);
        conv.setUser1(u1);
        conv.setUser2(u2);
        User sender = new User(); sender.setId(2L);
        when(messageMapper.toEntity(dto)).thenReturn(entity);
        when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
        when(userRepository.findById(2L)).thenReturn(Optional.of(sender));
        when(messageRepository.save(any())).thenReturn(entity);
        when(messageMapper.toDto(entity)).thenReturn(dto);
        MessageDTO result = service.sendMessage(dto);
        assertEquals("msg", result.getContent());
    }

    @Test
    void testSendMessageConversationNotFound() {
        MessageDTO dto = new MessageDTO(); dto.setConversationId(10L); dto.setSenderId(2L);
        when(messageMapper.toEntity(dto)).thenReturn(new Message());
        when(conversationRepository.findById(10L)).thenReturn(Optional.empty());
        
        assertThrows(ConversationNotFoundException.class, () -> service.sendMessage(dto));
    }

    @Test
    void testSendMessageSenderNotParticipant() {
        MessageDTO dto = new MessageDTO(); dto.setConversationId(10L); dto.setSenderId(3L);
        Message entity = new Message();
        Conversation conv = new Conversation(); conv.setId(10L);
        User u1 = new User(); u1.setId(1L); User u2 = new User(); u2.setId(2L);
        conv.setUser1(u1); conv.setUser2(u2);
        when(messageMapper.toEntity(dto)).thenReturn(entity);
        when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
        
        assertThrows(ConversationForbiddenException.class, () -> service.sendMessage(dto));
    }

    @Test
    void testSendMessageSenderNotFound() {
        MessageDTO dto = new MessageDTO(); dto.setConversationId(10L); dto.setSenderId(2L);
        Message entity = new Message();
        Conversation conv = new Conversation(); conv.setId(10L);
        User u1 = new User(); u1.setId(1L); User u2 = new User(); u2.setId(2L);
        conv.setUser1(u1); conv.setUser2(u2);
        when(messageMapper.toEntity(dto)).thenReturn(entity);
        when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> service.sendMessage(dto));
    }

    @Test
    void testUpdateMessageSuccess() {
        Message m = new Message(); m.setId(1L); User sender = new User(); sender.setId(2L); m.setSender(sender); m.setContent("old");
        MessageDTO dto = new MessageDTO(); dto.setContent("new");
        when(messageRepository.findById(1L)).thenReturn(Optional.of(m));
        when(messageRepository.save(any())).thenReturn(m);
        when(messageMapper.toDto(m)).thenReturn(dto);
        MessageDTO result = service.updateMessage(1L, dto, 2L);
        
        assertEquals("new", result.getContent());
    }

    @Test
    void testUpdateMessageNotFound() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());
        MessageDTO dto = new MessageDTO();
        
        assertThrows(ConversationNotFoundException.class, () -> service.updateMessage(1L, dto, 2L));
    }

    @Test
    void testUpdateMessageForbidden() {
        Message m = new Message(); m.setId(1L); User sender = new User(); sender.setId(2L); m.setSender(sender);
        when(messageRepository.findById(1L)).thenReturn(Optional.of(m));
        MessageDTO dto = new MessageDTO();
        
        assertThrows(ConversationForbiddenException.class, () -> service.updateMessage(1L, dto, 3L));
    }
} 