package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.ConversationAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException;
import com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.ConversationMapper;
import com.mastere_project.vacances_tranquilles.mapper.ReservationMapper;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ConversationRepository;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceImplTest {
    @Mock ConversationRepository conversationRepository;
    @Mock UserRepository userRepository;
    @Mock ReservationRepository reservationRepository;
    @Mock ConversationMapper conversationMapper;
    @Mock ReservationMapper reservationMapper;
    @InjectMocks ConversationServiceImpl service;

    @BeforeEach
    void setUp() { 
        lenient().when(conversationMapper.toDto(any())).thenReturn(new ConversationDTO());
    }

    @Test
    void testGetConversationsForUser() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            
            ConversationSummaryDto dto = new ConversationSummaryDto(1L, "User1", "Service1", LocalDate.now(), LocalTime.now());
            when(conversationRepository.findConversationsForUser(1L)).thenReturn(List.of(dto));
            
            List<ConversationSummaryDto> result = service.getConversationsForUser();
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).conversationId());
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
            
            when(conversationRepository.findConversationsForUser(1L)).thenReturn(List.of());
            
            List<ConversationSummaryDto> result = service.getConversationsForUser();
            assertEquals(0, result.size());
        }
    }

    @Test
    void testGetConversationsForUserUserNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(UserNotFoundException.class, () -> service.getConversationsForUser());
        }
    }

    @Test
    void testCreateConversationSuccess() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User client = new User(); 
            client.setId(1L);
            client.setUserRole(UserRole.CLIENT);
            User provider = new User(); 
            provider.setId(2L);
            provider.setUserRole(UserRole.PROVIDER);
            
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            reservation.setConversation(null); // Aucune conversation existante
            reservation.setClient(client);
            reservation.setProvider(provider);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(client));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            when(reservationRepository.save(any())).thenReturn(reservation);
            
            Conversation conv = new Conversation(); 
            conv.setId(10L); 
            conv.setUser1(client); 
            conv.setUser2(provider);
            when(conversationRepository.save(any())).thenReturn(conv);
            ConversationDTO dto = new ConversationDTO(); 
            dto.setId(10L);
            when(conversationMapper.toDto(conv)).thenReturn(dto);
            
            ConversationDTO result = service.createConversation(1L);
            assertEquals(10L, result.getId());
        }
    }

    @Test
    void testCreateConversationUserNotParticipant() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(3L);
            
            User currentUser = new User(); 
            currentUser.setId(3L);
            currentUser.setUserRole(UserRole.CLIENT);
            
            User client = new User(); 
            client.setId(1L);
            client.setUserRole(UserRole.CLIENT);
            User provider = new User(); 
            provider.setId(2L);
            provider.setUserRole(UserRole.PROVIDER);
            
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            reservation.setClient(client);
            reservation.setProvider(provider);
            
            when(userRepository.findById(3L)).thenReturn(Optional.of(currentUser));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            
            assertThrows(ConversationForbiddenException.class, () -> service.createConversation(1L));
        }
    }

    @Test
    void testCreateConversationUserNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> service.createConversation(1L));
        }
    }

    @Test
    void testCreateConversationReservationNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User client = new User(); 
            client.setId(1L);
            client.setUserRole(UserRole.CLIENT);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(client));
            when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(ReservationNotFoundException.class, () -> service.createConversation(1L));
        }
    }

    @Test
    void testCreateConversationReservationStatusInvalid() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User client = new User(); 
            client.setId(1L);
            client.setUserRole(UserRole.CLIENT);
            User provider = new User(); 
            provider.setId(2L);
            provider.setUserRole(UserRole.PROVIDER);
            
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.CLOSED);
            reservation.setClient(client);
            reservation.setProvider(provider);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(client));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            
            assertThrows(ConversationForbiddenException.class, () -> service.createConversation(1L));
        }
    }

    @Test
    void testCreateConversationAlreadyExists() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User client = new User(); 
            client.setId(1L);
            client.setUserRole(UserRole.CLIENT);
            User provider = new User(); 
            provider.setId(2L);
            provider.setUserRole(UserRole.PROVIDER);
            
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            reservation.setClient(client);
            reservation.setProvider(provider);
            
            // Créer une conversation existante pour cette réservation
            Conversation existingConversation = new Conversation();
            existingConversation.setId(5L);
            existingConversation.setUser1(client);
            existingConversation.setUser2(provider);
            reservation.setConversation(existingConversation);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(client));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            
            assertThrows(ConversationAlreadyExistsException.class, () -> service.createConversation(1L));
        }
    }

    @Test
    void testCreateConversationInvalidRoles() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User client1 = new User(); 
            client1.setId(1L);
            client1.setUserRole(UserRole.CLIENT);
            User client2 = new User(); 
            client2.setId(2L);
            client2.setUserRole(UserRole.CLIENT); // Deux clients ne peuvent pas avoir de conversation
            
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            reservation.setConversation(null);
            reservation.setClient(client1);
            reservation.setProvider(client2); // Provider avec rôle CLIENT (invalide)
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(client1));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            
            assertThrows(ConversationForbiddenException.class, () -> service.createConversation(1L));
        }
    }

    @Test
    void testCreateConversationNullReservationId() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            assertThrows(IllegalArgumentException.class, () -> service.createConversation(null));
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
    void testGetConversationByIdNullId() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            assertThrows(IllegalArgumentException.class, () -> service.getConversationById(null));
        }
    }

    @Test
    void testGetConversationByIdUserNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(UserNotFoundException.class, () -> service.getConversationById(1L));
        }
    }

    @Test
    void testCreateMultipleConversationsBetweenSameUsers() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User client = new User(); 
            client.setId(1L);
            client.setUserRole(UserRole.CLIENT);
            User provider = new User(); 
            provider.setId(2L);
            provider.setUserRole(UserRole.PROVIDER);
            
            // Première réservation sans conversation
            Reservation reservation1 = new Reservation();
            reservation1.setId(1L);
            reservation1.setStatus(ReservationStatus.IN_PROGRESS);
            reservation1.setConversation(null);
            reservation1.setClient(client);
            reservation1.setProvider(provider);
            
            // Deuxième réservation sans conversation
            Reservation reservation2 = new Reservation();
            reservation2.setId(2L);
            reservation2.setStatus(ReservationStatus.IN_PROGRESS);
            reservation2.setConversation(null);
            reservation2.setClient(client);
            reservation2.setProvider(provider);
            
            when(userRepository.findById(1L)).thenReturn(Optional.of(client));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));
            when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation2));
            when(reservationRepository.save(any())).thenReturn(reservation1).thenReturn(reservation2);
            
            Conversation conv1 = new Conversation(); 
            conv1.setId(10L); 
            conv1.setUser1(client); 
            conv1.setUser2(provider);
            Conversation conv2 = new Conversation(); 
            conv2.setId(11L); 
            conv2.setUser1(client); 
            conv2.setUser2(provider);
            
            when(conversationRepository.save(any())).thenReturn(conv1).thenReturn(conv2);
            when(conversationMapper.toDto(conv1)).thenReturn(new ConversationDTO());
            when(conversationMapper.toDto(conv2)).thenReturn(new ConversationDTO());
            
            // Créer deux conversations pour des réservations différentes
            ConversationDTO result1 = service.createConversation(1L);
            ConversationDTO result2 = service.createConversation(2L);
            
            // Les deux conversations doivent être créées avec succès
            assertNotNull(result1);
            assertNotNull(result2);
            
            // Vérifier que save() a été appelé deux fois
            verify(conversationRepository, times(2)).save(any());
        }
    }

    @Test
    void testCreateConversationAsProvider() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
            
            User client = new User(); 
            client.setId(1L);
            client.setUserRole(UserRole.CLIENT);
            User provider = new User(); 
            provider.setId(2L);
            provider.setUserRole(UserRole.PROVIDER);
            
            Reservation reservation = new Reservation();
            reservation.setId(1L);
            reservation.setStatus(ReservationStatus.IN_PROGRESS);
            reservation.setConversation(null);
            reservation.setClient(client);
            reservation.setProvider(provider);
            
            when(userRepository.findById(2L)).thenReturn(Optional.of(provider));
            when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
            when(reservationRepository.save(any())).thenReturn(reservation);
            
            Conversation conv = new Conversation(); 
            conv.setId(10L); 
            conv.setUser1(provider); 
            conv.setUser2(client);
            when(conversationRepository.save(any())).thenReturn(conv);
            ConversationDTO dto = new ConversationDTO(); 
            dto.setId(10L);
            when(conversationMapper.toDto(conv)).thenReturn(dto);
            
            ConversationDTO result = service.createConversation(1L);
            assertEquals(10L, result.getId());
        }
    }

    @Test
    void testGetReservationByConversationIdSuccess() {
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
            
            Reservation reservation = new Reservation();
            reservation.setId(10L);
            reservation.setConversation(conv);
            when(reservationRepository.findByConversationId(1L)).thenReturn(Optional.of(reservation));
            
            ReservationResponseDTO dto = new ReservationResponseDTO();
            dto.setId(10L);
            when(reservationMapper.toResponseDTO(reservation)).thenReturn(dto);
            
            ReservationResponseDTO result = service.getReservationByConversationId(1L);
            assertEquals(10L, result.getId());
        }
    }

    @Test
    void testGetReservationByConversationIdConversationNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            User currentUser = new User(); 
            currentUser.setId(1L);
            currentUser.setUserRole(UserRole.CLIENT);
            when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
            when(conversationRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(ConversationNotFoundException.class, () -> service.getReservationByConversationId(1L));
        }
    }

    @Test
    void testGetReservationByConversationIdForbidden() {
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
            
            assertThrows(ConversationForbiddenException.class, () -> service.getReservationByConversationId(1L));
        }
    }

    @Test
    void testGetReservationByConversationIdReservationNotFound() {
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
            when(reservationRepository.findByConversationId(1L)).thenReturn(Optional.empty());
            
            assertThrows(ReservationNotFoundException.class, () -> service.getReservationByConversationId(1L));
        }
    }

    @Test
    void testGetReservationByConversationIdNullId() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            assertThrows(IllegalArgumentException.class, () -> service.getReservationByConversationId(null));
        }
    }

    @Test
    void testGetReservationByConversationIdUserNotFound() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(UserNotFoundException.class, () -> service.getReservationByConversationId(1L));
        }
    }
} 