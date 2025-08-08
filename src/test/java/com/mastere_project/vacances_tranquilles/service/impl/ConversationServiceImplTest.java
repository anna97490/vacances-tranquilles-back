package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.exception.ConversationAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException;
import com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.ConversationMapper;
import com.mastere_project.vacances_tranquilles.repository.ConversationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

class ConversationServiceImplTest {
    @Mock ConversationRepository conversationRepository;
    @Mock UserRepository userRepository;
    @Mock ReservationRepository reservationRepository;
    @Mock ConversationMapper conversationMapper;
    @InjectMocks ConversationServiceImpl service;

    @BeforeEach
    void setUp() { 
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testGetConversationsForUser() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            
            Conversation conv = new Conversation(); 
            conv.setId(1L);
            when(conversationRepository.findByUser1IdOrUser2Id(1L, 1L)).thenReturn(List.of(conv));
            ConversationDTO dto = new ConversationDTO(); 
            dto.setId(1L);
            when(conversationMapper.toDto(conv)).thenReturn(dto);
            
            List<ConversationDTO> result = service.getConversationsForUser();
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getId());
        }
    }

    @Test
    void testGetConversationsForUserEmptyList() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            
            when(conversationRepository.findByUser1IdOrUser2Id(1L, 1L)).thenReturn(List.of());
            
            List<ConversationDTO> result = service.getConversationsForUser();
            assertEquals(0, result.size());
        }
    }

    @Test
    void testCreateConversationSuccess() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User u1 = new User(); 
            u1.setId(1L);
            u1.setUserRole(UserRole.CLIENT);
            User u2 = new User(); 
            u2.setId(2L);
            u2.setUserRole(UserRole.PROVIDER);
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
            when(userRepository.findById(2L)).thenReturn(Optional.of(u2));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            when(conversationRepository.findByUser1IdAndUser2Id(1L, 2L)).thenReturn(Optional.empty());
            when(conversationRepository.findByUser2IdAndUser1Id(1L, 2L)).thenReturn(Optional.empty());
            
            Conversation conv = new Conversation(); 
            conv.setId(10L); 
            conv.setUser1(u1); 
            conv.setUser2(u2);
            when(conversationRepository.save(any())).thenReturn(conv);
            ConversationDTO dto = new ConversationDTO(); 
            dto.setId(10L);
            when(conversationMapper.toDto(conv)).thenReturn(dto);
            
            ConversationDTO result = service.createConversation(2L, 1L);
            assertEquals(10L, result.getId());
        }
    }

    @Test
    void testCreateConversationWithSelfThrows() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            
            assertThrows(ConversationForbiddenException.class, () -> service.createConversation(1L, 1L));
        }
    }

    @Test
    void testCreateConversationWithSameRole() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User u1 = new User(); 
            u1.setId(1L);
            u1.setUserRole(UserRole.CLIENT);
            User u2 = new User(); 
            u2.setId(2L);
            u2.setUserRole(UserRole.CLIENT); // Same role
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
            when(userRepository.findById(2L)).thenReturn(Optional.of(u2));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            
            assertThrows(ConversationForbiddenException.class, () -> service.createConversation(2L, 1L));
        }
    }

    @Test
    void testCreateConversationUserNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> service.createConversation(2L, 1L));
        }
    }

    @Test
    void testCreateConversationOtherUserNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User u1 = new User(); 
            u1.setId(1L);
            u1.setUserRole(UserRole.CLIENT);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
            when(userRepository.findById(2L)).thenReturn(Optional.empty());
            
            assertThrows(UserNotFoundException.class, () -> service.createConversation(2L, 1L));
        }
    }

    @Test
    void testCreateConversationReservationNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User u1 = new User(); 
            u1.setId(1L);
            u1.setUserRole(UserRole.CLIENT);
            User u2 = new User(); 
            u2.setId(2L);
            u2.setUserRole(UserRole.PROVIDER);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
            when(userRepository.findById(2L)).thenReturn(Optional.of(u2));
            when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(ReservationNotFoundException.class, () -> service.createConversation(2L, 1L));
        }
    }

    @Test
    void testCreateConversationReservationStatusInvalid() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User u1 = new User(); 
            u1.setId(1L);
            u1.setUserRole(UserRole.CLIENT);
            User u2 = new User(); 
            u2.setId(2L);
            u2.setUserRole(UserRole.PROVIDER);
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.CLOSED);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
            when(userRepository.findById(2L)).thenReturn(Optional.of(u2));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            
            assertThrows(ConversationForbiddenException.class, () -> service.createConversation(2L, 1L));
        }
    }

    @Test
    void testCreateConversationAlreadyExists() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User u1 = new User(); 
            u1.setId(1L);
            u1.setUserRole(UserRole.CLIENT);
            User u2 = new User(); 
            u2.setId(2L);
            u2.setUserRole(UserRole.PROVIDER);
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
            when(userRepository.findById(2L)).thenReturn(Optional.of(u2));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            when(conversationRepository.findByUser1IdAndUser2Id(1L, 2L)).thenReturn(Optional.of(new Conversation()));
            
            assertThrows(ConversationAlreadyExistsException.class, () -> service.createConversation(2L, 1L));
        }
    }

    @Test
    void testGetConversationByIdSuccess() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            
            Conversation conv = new Conversation(); 
            conv.setId(1L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1); 
            conv.setUser2(u2);
            when(conversationRepository.findById(1L)).thenReturn(Optional.of(conv));
            ConversationDTO dto = new ConversationDTO(); 
            dto.setId(1L);
            when(conversationMapper.toDto(conv)).thenReturn(dto);
            
            ConversationDTO result = service.getConversationById(1L);
            assertEquals(1L, result.getId());
        }
    }

    @Test
    void testGetConversationByIdNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            when(conversationRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(ConversationNotFoundException.class, () -> service.getConversationById(1L));
        }
    }

    @Test
    void testGetConversationByIdForbidden() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(3L);
            
            User currentUser = new User(); 
            currentUser.setId(3L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(3L)).thenReturn(Optional.of(currentUser));
            
            Conversation conv = new Conversation(); 
            conv.setId(1L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1); 
            conv.setUser2(u2);
            when(conversationRepository.findById(1L)).thenReturn(Optional.of(conv));
            
            assertThrows(ConversationForbiddenException.class, () -> service.getConversationById(1L));
        }
    }

    @Test
    void testGetConversationByIdUserNotParticipant() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(3L);
            
            User currentUser = new User(); 
            currentUser.setId(3L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(3L)).thenReturn(Optional.of(currentUser));
            
            Conversation conv = new Conversation(); 
            conv.setId(1L);
            User u1 = new User(); 
            u1.setId(1L);
            User u2 = new User(); 
            u2.setId(2L);
            conv.setUser1(u1); 
            conv.setUser2(u2);
            when(conversationRepository.findById(1L)).thenReturn(Optional.of(conv));
            
            assertThrows(ConversationForbiddenException.class, () -> service.getConversationById(1L));
        }
    }
} 