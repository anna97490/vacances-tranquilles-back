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
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
    void setUp() { 
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testGetMessagesByConversationIdMarksRead() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            currentUser.setFirstName("John");
            currentUser.setLastName("Doe");
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            when(userRepository.getReferenceById(1L)).thenReturn(currentUser);
            
            Message m1 = new Message(); 
            m1.setId(1L); 
            m1.setRead(false); 
            User sender1 = new User();
            sender1.setId(2L);
            m1.setSender(sender1);
            Message m2 = new Message(); 
            m2.setId(2L); 
            m2.setRead(true); 
            User sender2 = new User();
            sender2.setId(1L);
            m2.setSender(sender2);
            Conversation conv = new Conversation(); 
            conv.setId(10L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1);
            conv.setUser2(u2);
            
            when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
            when(messageRepository.findByConversationIdOrderBySentAtAsc(10L)).thenReturn(List.of(m1, m2));
            when(messageRepository.saveAll(any())).thenReturn(List.of(m1, m2));
            when(messageRepository.markMessagesAsRead(10L, 1L)).thenReturn(1);
            when(messageRepository.findMessagesDTOByConversationId(10L, "John Doe")).thenReturn(List.of());
            
            List<MessageResponseDTO> result = service.getMessagesByConversationId(10L);
            
            assertEquals(0, result.size());
            verify(messageRepository).markMessagesAsRead(10L, 1L);
        }
    }

    @Test
    void testSendMessageSuccess() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            MessageDTO dto = new MessageDTO(); 
            dto.setConversationId(10L); 
            dto.setContent("msg");
            Message entity = new Message(); 
            entity.setId(1L); 
            entity.setContent("msg");
            Conversation conv = new Conversation(); 
            conv.setId(10L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1);
            conv.setUser2(u2);
            User sender = new User(); 
            sender.setId(2L);
            
            when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
            when(messageMapper.toEntity(dto)).thenReturn(entity);
            when(messageRepository.save(any())).thenReturn(entity);
            when(messageMapper.toDto(entity)).thenReturn(dto);
            
            MessageDTO result = service.sendMessage(dto);
            assertEquals("msg", result.getContent());
        }
    }

    @Test
    void testSendMessageConversationNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            MessageDTO dto = new MessageDTO(); 
            dto.setConversationId(10L);
            dto.setContent("test message");
            when(messageMapper.toEntity(dto)).thenReturn(new Message());
            when(conversationRepository.findById(10L)).thenReturn(Optional.empty());
            
            assertThrows(ConversationNotFoundException.class, () -> service.sendMessage(dto));
        }
    }

    @Test
    void testSendMessageSenderNotParticipant() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(3L);
            
            User currentUser = new User(); 
            currentUser.setId(3L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(3L)).thenReturn(Optional.of(currentUser));
            
            MessageDTO dto = new MessageDTO(); 
            dto.setConversationId(10L);
            dto.setContent("test message");
            Message entity = new Message();
            Conversation conv = new Conversation(); 
            conv.setId(10L);
            User u1 = new User(); 
            u1.setId(1L); 
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1); 
            conv.setUser2(u2);
            
            when(messageMapper.toEntity(dto)).thenReturn(entity);
            when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
            
            assertThrows(ConversationForbiddenException.class, () -> service.sendMessage(dto));
        }
    }

    @Test
    void testSendMessageSenderNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            when(userRepository.findById(2L)).thenReturn(Optional.empty());
            
            MessageDTO dto = new MessageDTO(); 
            dto.setConversationId(10L);
            dto.setContent("test message");
            Message entity = new Message();
            Conversation conv = new Conversation(); 
            conv.setId(10L);
            User u1 = new User(); 
            u1.setId(1L); 
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1); 
            conv.setUser2(u2);
            
            when(messageMapper.toEntity(dto)).thenReturn(entity);
            when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
            
            assertThrows(UserNotFoundException.class, () -> service.sendMessage(dto));
        }
    }

    @Test
    void testUpdateMessageSuccess() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            Message m = new Message(); 
            m.setId(1L); 
            User sender = new User(); 
            sender.setId(2L); 
            m.setSender(sender); 
            m.setContent("old");
            MessageDTO dto = new MessageDTO(); 
            dto.setContent("new");
            
            when(messageRepository.findById(1L)).thenReturn(Optional.of(m));
            when(messageRepository.save(any())).thenReturn(m);
            when(messageMapper.toDto(m)).thenReturn(dto);
            
            MessageDTO result = service.updateMessage(1L, dto);
            
            assertEquals("new", result.getContent());
        }
    }

    @Test
    void testUpdateMessageNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            when(messageRepository.findById(1L)).thenReturn(Optional.empty());
            MessageDTO dto = new MessageDTO();
            dto.setContent("test message");
            
            assertThrows(ConversationNotFoundException.class, () -> service.updateMessage(1L, dto));
        }
    }

    @Test
    void testUpdateMessageForbidden() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(3L);
            
            User currentUser = new User(); 
            currentUser.setId(3L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(3L)).thenReturn(Optional.of(currentUser));
            
            Message m = new Message(); 
            m.setId(1L); 
            User sender = new User(); 
            sender.setId(2L); 
            m.setSender(sender);
            
            when(messageRepository.findById(1L)).thenReturn(Optional.of(m));
            MessageDTO dto = new MessageDTO();
            dto.setContent("test message");
            
            assertThrows(ConversationForbiddenException.class, () -> service.updateMessage(1L, dto));
        }
    }

    @Test
    void testGetMessagesByConversationIdNoMessages() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            currentUser.setFirstName("John");
            currentUser.setLastName("Doe");
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            when(userRepository.getReferenceById(1L)).thenReturn(currentUser);
            
            Conversation conv = new Conversation(); 
            conv.setId(10L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1);
            conv.setUser2(u2);
            
            when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
            when(messageRepository.findByConversationIdOrderBySentAtAsc(10L)).thenReturn(List.of());
            when(messageRepository.markMessagesAsRead(10L, 1L)).thenReturn(0);
            when(messageRepository.findMessagesDTOByConversationId(10L, "John Doe")).thenReturn(List.of());
            
            List<MessageResponseDTO> result = service.getMessagesByConversationId(10L);
            
            assertEquals(0, result.size());
        }
    }

    @Test
    void testGetMessagesByConversationIdNoUnreadMessages() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            currentUser.setFirstName("John");
            currentUser.setLastName("Doe");
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            when(userRepository.getReferenceById(1L)).thenReturn(currentUser);
            
            Message m1 = new Message(); 
            m1.setId(1L); 
            User sender = new User(); 
            sender.setId(2L); 
            m1.setSender(sender); 
            m1.setRead(true); // Already read
            Message m2 = new Message(); 
            m2.setId(2L); 
            User sender2 = new User(); 
            sender2.setId(1L); 
            m2.setSender(sender2); 
            m2.setRead(false); // Own message, shouldn't be marked as read
            
            Conversation conv = new Conversation(); 
            conv.setId(10L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1);
            conv.setUser2(u2);
            
            when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
            when(messageRepository.findByConversationIdOrderBySentAtAsc(10L)).thenReturn(List.of(m1, m2));
            when(messageRepository.markMessagesAsRead(10L, 1L)).thenReturn(0);
            when(messageRepository.findMessagesDTOByConversationId(10L, "John Doe")).thenReturn(List.of());
            
            List<MessageResponseDTO> result = service.getMessagesByConversationId(10L);
            
            assertEquals(0, result.size());
            verify(messageRepository).markMessagesAsRead(10L, 1L);
        }
    }

    @Test
    void testSendMessageWithNullSentAt() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            MessageDTO dto = new MessageDTO(); 
            dto.setConversationId(10L); 
            dto.setContent("msg");
            dto.setSentAt(null); // Test null sentAt
            Message entity = new Message(); 
            entity.setId(1L); 
            entity.setContent("msg");
            entity.setSentAt(null);
            Conversation conv = new Conversation(); 
            conv.setId(10L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1);
            conv.setUser2(u2);
            
            when(conversationRepository.findById(10L)).thenReturn(Optional.of(conv));
            when(messageMapper.toEntity(dto)).thenReturn(entity);
            when(messageRepository.save(any())).thenReturn(entity);
            when(messageMapper.toDto(entity)).thenReturn(dto);
            
            MessageDTO result = service.sendMessage(dto);
            assertEquals("msg", result.getContent());
        }
    }

    @Test
    void testUpdateMessageWithNullContent() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            Message m = new Message(); 
            m.setId(1L); 
            User sender = new User(); 
            sender.setId(2L); 
            m.setSender(sender); 
            m.setContent("old");
            MessageDTO dto = new MessageDTO(); 
            dto.setContent(null); // Test null content
            
            when(messageRepository.findById(1L)).thenReturn(Optional.of(m));
            
            assertThrows(IllegalArgumentException.class, () -> service.updateMessage(1L, dto));
        }
    }

    @Test
    void testSendMessageWithNullContent() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            MessageDTO dto = new MessageDTO(); 
            dto.setConversationId(10L); 
            dto.setContent(null); // Test null content
            
            assertThrows(IllegalArgumentException.class, () -> service.sendMessage(dto));
        }
    }

    @Test
    void testSendMessageWithEmptyContent() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            MessageDTO dto = new MessageDTO(); 
            dto.setConversationId(10L); 
            dto.setContent(""); // Test empty content
            
            assertThrows(IllegalArgumentException.class, () -> service.sendMessage(dto));
        }
    }

    @Test
    void testUpdateMessageWithEmptyContent() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User currentUser = new User(); 
            currentUser.setId(2L);
            currentUser.setUserRole(UserRole.PROVIDER);
            when(userRepository.findById(2L)).thenReturn(Optional.of(currentUser));
            
            Message m = new Message(); 
            m.setId(1L); 
            User sender = new User(); 
            sender.setId(2L); 
            m.setSender(sender); 
            m.setContent("old");
            MessageDTO dto = new MessageDTO(); 
            dto.setContent(""); // Test empty content
            
            when(messageRepository.findById(1L)).thenReturn(Optional.of(m));
            
            assertThrows(IllegalArgumentException.class, () -> service.updateMessage(1L, dto));
        }
    }
} 