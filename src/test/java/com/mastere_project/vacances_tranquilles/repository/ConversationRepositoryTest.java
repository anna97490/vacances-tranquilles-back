package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ConversationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConversationRepository conversationRepository;

    private User client;
    private User provider;
    private Service service;
    private Conversation conversation;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Créer un client
        client = new User();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUserRole(UserRole.CLIENT);
        client.setEmail("john.doe@test.com");
        client.setPassword("password123");
        client.setPhoneNumber("0123456789");
        client.setAddress("123 Test Street");
        client.setCity("Test City");
        client.setPostalCode("12345");
        client = entityManager.persistAndFlush(client);

        // Créer un prestataire
        provider = new User();
        provider.setFirstName("Jane");
        provider.setLastName("Smith");
        provider.setUserRole(UserRole.PROVIDER);
        provider.setEmail("jane.smith@test.com");
        provider.setPassword("password123");
        provider.setPhoneNumber("0987654321");
        provider.setAddress("456 Provider Street");
        provider.setCity("Provider City");
        provider.setPostalCode("54321");
        provider.setCompanyName("Test Company");
        provider.setSiretSiren("12345678900000");
        provider = entityManager.persistAndFlush(provider);

        // Créer un service
        service = new Service();
        service.setTitle("Service de test");
        service.setPrice(new BigDecimal("100.0")); // Prix obligatoire
        service.setDescription("Description du service de test");
        service.setCategory("Test Category");
        service.setProvider(provider); // Associer le prestataire
        service = entityManager.persistAndFlush(service);

        // Créer une conversation
        conversation = new Conversation();
        conversation.setUser1(client);
        conversation.setUser2(provider);
        conversation = entityManager.persistAndFlush(conversation);

        // Créer une réservation
        reservation = new Reservation();
        reservation.setClient(client);
        reservation.setProvider(provider);
        reservation.setService(service);
        reservation.setConversation(conversation);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStartDate(LocalTime.of(10, 0));
        reservation.setEndDate(LocalTime.of(12, 0));
        reservation = entityManager.persistAndFlush(reservation);
    }

    @Test
    void findConversationsForUser_shouldReturnConversationsForClient() {
        // When
        List<ConversationSummaryDto> result = conversationRepository.findConversationsForUser(client.getId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        ConversationSummaryDto dto = result.get(0);
        assertEquals(conversation.getId(), dto.conversationId());
        assertEquals("Jane Smith", dto.otherUserName()); // Nom du prestataire
        assertEquals("Service de test", dto.serviceTitle());
        assertEquals(LocalDate.now(), dto.reservationDate());
        assertEquals(LocalTime.of(10, 0), dto.startTime());
    }

    @Test
    void findConversationsForUser_shouldReturnConversationsForProvider() {
        // When
        List<ConversationSummaryDto> result = conversationRepository.findConversationsForUser(provider.getId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        ConversationSummaryDto dto = result.get(0);
        assertEquals(conversation.getId(), dto.conversationId());
        assertEquals("John Doe", dto.otherUserName()); // Nom du client
        assertEquals("Service de test", dto.serviceTitle());
        assertEquals(LocalDate.now(), dto.reservationDate());
        assertEquals(LocalTime.of(10, 0), dto.startTime());
    }

    @Test
    void findConversationsForUser_shouldReturnEmptyList_whenUserHasNoConversations() {
        // Given
        User otherUser = new User();
        otherUser.setFirstName("Other");
        otherUser.setLastName("User");
        otherUser.setUserRole(UserRole.CLIENT);
        otherUser.setEmail("other.user@test.com");
        otherUser.setPassword("password123");
        otherUser.setPhoneNumber("1111111111");
        otherUser.setAddress("789 Other Street");
        otherUser.setCity("Other City");
        otherUser.setPostalCode("99999");
        entityManager.persistAndFlush(otherUser);

        // When
        List<ConversationSummaryDto> result = conversationRepository.findConversationsForUser(otherUser.getId());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUser1IdOrUser2Id_shouldReturnConversations() {
        // When
        List<Conversation> result = conversationRepository.findByUser1IdOrUser2Id(client.getId(), client.getId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(conversation.getId(), result.get(0).getId());
    }

    @Test
    void findByUser1IdOrUser2Id_shouldReturnEmptyList_whenNoConversations() {
        // Given
        User otherUser = new User();
        otherUser.setFirstName("Other");
        otherUser.setLastName("User");
        otherUser.setUserRole(UserRole.CLIENT);
        otherUser.setEmail("other.user2@test.com");
        otherUser.setPassword("password123");
        otherUser.setPhoneNumber("2222222222");
        otherUser.setAddress("789 Other Street 2");
        otherUser.setCity("Other City 2");
        otherUser.setPostalCode("88888");
        entityManager.persistAndFlush(otherUser);

        // When
        List<Conversation> result = conversationRepository.findByUser1IdOrUser2Id(otherUser.getId(), otherUser.getId());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUser1IdAndUser2Id_shouldReturnConversation() {
        // When
        Optional<Conversation> result = conversationRepository.findByUser1IdAndUser2Id(client.getId(), provider.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(conversation.getId(), result.get().getId());
    }

    @Test
    void findByUser1IdAndUser2Id_shouldReturnEmpty_whenNoConversation() {
        // Given
        User otherUser = new User();
        otherUser.setFirstName("Other");
        otherUser.setLastName("User");
        otherUser.setUserRole(UserRole.CLIENT);
        otherUser.setEmail("other.user3@test.com");
        otherUser.setPassword("password123");
        otherUser.setPhoneNumber("3333333333");
        otherUser.setAddress("789 Other Street 3");
        otherUser.setCity("Other City 3");
        otherUser.setPostalCode("77777");
        entityManager.persistAndFlush(otherUser);

        // When
        Optional<Conversation> result = conversationRepository.findByUser1IdAndUser2Id(client.getId(), otherUser.getId());

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void findByUser2IdAndUser1Id_shouldReturnConversation() {
        // When
        Optional<Conversation> result = conversationRepository.findByUser2IdAndUser1Id(provider.getId(), client.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(conversation.getId(), result.get().getId());
    }

    @Test
    void findByUser2IdAndUser1Id_shouldReturnEmpty_whenNoConversation() {
        // Given
        User otherUser = new User();
        otherUser.setFirstName("Other");
        otherUser.setLastName("User");
        otherUser.setUserRole(UserRole.CLIENT);
        otherUser.setEmail("other.user4@test.com");
        otherUser.setPassword("password123");
        otherUser.setPhoneNumber("4444444444");
        otherUser.setAddress("789 Other Street 4");
        otherUser.setCity("Other City 4");
        otherUser.setPostalCode("66666");
        entityManager.persistAndFlush(otherUser);

        // When
        Optional<Conversation> result = conversationRepository.findByUser2IdAndUser1Id(provider.getId(), otherUser.getId());

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void findConversationsForUser_shouldOrderByDateDesc() {
        // Given - Créer une deuxième réservation avec une date antérieure
        Reservation oldReservation = new Reservation();
        oldReservation.setClient(client);
        oldReservation.setProvider(provider);
        oldReservation.setService(service);
        oldReservation.setReservationDate(LocalDate.now().minusDays(1));
        oldReservation.setStartDate(LocalTime.of(9, 0));
        oldReservation.setEndDate(LocalTime.of(11, 0));
        entityManager.persistAndFlush(oldReservation);

        Conversation oldConversation = new Conversation();
        oldConversation.setUser1(client);
        oldConversation.setUser2(provider);
        oldConversation = entityManager.persistAndFlush(oldConversation);
        oldReservation.setConversation(oldConversation);
        entityManager.persistAndFlush(oldReservation);

        // When
        List<ConversationSummaryDto> result = conversationRepository.findConversationsForUser(client.getId());

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // La première conversation devrait être la plus récente
        assertEquals(conversation.getId(), result.get(0).conversationId());
        assertEquals(LocalDate.now(), result.get(0).reservationDate());
        
        // La deuxième conversation devrait être la plus ancienne
        assertEquals(oldConversation.getId(), result.get(1).conversationId());
        assertEquals(LocalDate.now().minusDays(1), result.get(1).reservationDate());
    }
}
