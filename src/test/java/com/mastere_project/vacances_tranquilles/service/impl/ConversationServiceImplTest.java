package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.ConversationAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException;
import com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.ConversationMapper;
import com.mastere_project.vacances_tranquilles.repository.ConversationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConversationServiceImplTest {
    @Mock ConversationRepository conversationRepository;
    @Mock UserRepository userRepository;
    @Mock ConversationMapper conversationMapper;
    @InjectMocks ConversationServiceImpl service;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGetConversationsForUser() {
        Conversation conv = new Conversation(); conv.setId(1L);
        when(conversationRepository.findByUser1IdOrUser2Id(1L, 1L)).thenReturn(List.of(conv));
        ConversationDTO dto = new ConversationDTO(); dto.setId(1L);
        when(conversationMapper.toDto(conv)).thenReturn(dto);
        List<ConversationDTO> result = service.getConversationsForUser(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testCreateConversationSuccess() {
        User u1 = new User(); u1.setId(1L);
        User u2 = new User(); u2.setId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(u2));
        when(conversationRepository.findByUser1IdAndUser2Id(1L, 2L)).thenReturn(Optional.empty());
        when(conversationRepository.findByUser2IdAndUser1Id(2L, 1L)).thenReturn(Optional.empty());
        Conversation conv = new Conversation(); conv.setId(10L); conv.setUser1(u1); conv.setUser2(u2);
        when(conversationRepository.save(any())).thenReturn(conv);
        ConversationDTO dto = new ConversationDTO(); dto.setId(10L);
        when(conversationMapper.toDto(conv)).thenReturn(dto);
        ConversationDTO result = service.createConversation(1L, 2L);
        assertEquals(10L, result.getId());
    }

    @Test
    void testCreateConversationWithSelfThrows() {
        assertThrows(ConversationForbiddenException.class, () -> service.createConversation(1L, 1L));
    }

    @Test
    void testCreateConversationUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.createConversation(1L, 2L));
    }

    @Test
    void testCreateConversationAlreadyExists() {
        User u1 = new User(); u1.setId(1L);
        User u2 = new User(); u2.setId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(u2));
        when(conversationRepository.findByUser1IdAndUser2Id(1L, 2L)).thenReturn(Optional.of(new Conversation()));
        assertThrows(ConversationAlreadyExistsException.class, () -> service.createConversation(1L, 2L));
    }

    @Test
    void testGetConversationByIdSuccess() {
        Conversation conv = new Conversation(); conv.setId(1L);
        User u1 = new User(); u1.setId(1L);
        User u2 = new User(); u2.setId(2L);
        conv.setUser1(u1); conv.setUser2(u2);
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conv));
        ConversationDTO dto = new ConversationDTO(); dto.setId(1L);
        when(conversationMapper.toDto(conv)).thenReturn(dto);
        ConversationDTO result = service.getConversationById(1L, 1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetConversationByIdNotFound() {
        when(conversationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ConversationNotFoundException.class, () -> service.getConversationById(1L, 1L));
    }

    @Test
    void testGetConversationByIdForbidden() {
        Conversation conv = new Conversation(); conv.setId(1L);
        User u1 = new User(); u1.setId(1L);
        User u2 = new User(); u2.setId(2L);
        conv.setUser1(u1); conv.setUser2(u2);
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conv));
        assertThrows(ConversationForbiddenException.class, () -> service.getConversationById(1L, 3L));
    }
} 