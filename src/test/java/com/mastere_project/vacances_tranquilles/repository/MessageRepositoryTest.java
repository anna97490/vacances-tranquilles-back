package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.Message;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    private User sender;
    private User receiver;
    private Conversation conversation;
    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    void setUp() {
        // Créer un expéditeur
        sender = new User();
        sender.setFirstName("John");
        sender.setLastName("Doe");
        sender.setUserRole(UserRole.CLIENT);
        sender.setEmail("john.doe@test.com");
        sender.setPassword("password123");
        sender.setPhoneNumber("0123456789");
        sender.setAddress("123 Test Street");
        sender.setCity("Test City");
        sender.setPostalCode("12345");
        sender = entityManager.persistAndFlush(sender);

        // Créer un destinataire
        receiver = new User();
        receiver.setFirstName("Jane");
        receiver.setLastName("Smith");
        receiver.setUserRole(UserRole.PROVIDER);
        receiver.setEmail("jane.smith@test.com");
        receiver.setPassword("password123");
        receiver.setPhoneNumber("0987654321");
        receiver.setAddress("456 Provider Street");
        receiver.setCity("Provider City");
        receiver.setPostalCode("54321");
        receiver.setCompanyName("Test Company");
        receiver.setSiretSiren("12345678900000");
        receiver = entityManager.persistAndFlush(receiver);

        // Créer une conversation
        conversation = new Conversation();
        conversation.setUser1(sender);
        conversation.setUser2(receiver);
        conversation = entityManager.persistAndFlush(conversation);

        // Créer des messages
        message1 = new Message();
        message1.setConversation(conversation);
        message1.setSender(sender);
        message1.setContent("Premier message");
        message1.setSentAt(LocalDateTime.now().minusHours(2));
        message1.setRead(false);
        message1 = entityManager.persistAndFlush(message1);

        message2 = new Message();
        message2.setConversation(conversation);
        message2.setSender(receiver);
        message2.setContent("Deuxième message");
        message2.setSentAt(LocalDateTime.now().minusHours(1));
        message2.setRead(false);
        message2 = entityManager.persistAndFlush(message2);

        message3 = new Message();
        message3.setConversation(conversation);
        message3.setSender(sender);
        message3.setContent("Troisième message");
        message3.setSentAt(LocalDateTime.now());
        message3.setRead(true);
        message3 = entityManager.persistAndFlush(message3);
    }

    @Test
    void findByConversationIdOrderBySentAtAsc_shouldReturnMessagesInOrder() {
        List<Message> result = messageRepository.findByConversationIdOrderBySentAtAsc(conversation.getId());

        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Vérifier l'ordre chronologique
        assertEquals(message1.getId(), result.get(0).getId());
        assertEquals(message2.getId(), result.get(1).getId());
        assertEquals(message3.getId(), result.get(2).getId());
        
        // Vérifier que les messages appartiennent à la bonne conversation
        result.forEach(message -> assertEquals(conversation.getId(), message.getConversation().getId()));
    }

    @Test
    void findByConversationIdOrderBySentAtAsc_shouldReturnEmptyList_whenNoMessages() {
        // Créer une conversation sans messages
        Conversation emptyConversation = new Conversation();
        emptyConversation.setUser1(sender);
        emptyConversation.setUser2(receiver);
        emptyConversation = entityManager.persistAndFlush(emptyConversation);

        List<Message> result = messageRepository.findByConversationIdOrderBySentAtAsc(emptyConversation.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findMessagesDTOByConversationId_shouldReturnMessageDTOs() {
        List<MessageResponseDTO> result = messageRepository.findMessagesDTOByConversationId(conversation.getId(), "John Doe");

        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Vérifier le premier message
        MessageResponseDTO firstMessage = result.get(0);
        assertEquals("John Doe", firstMessage.getSenderName());
        assertEquals("Premier message", firstMessage.getContent());
        assertEquals(false, firstMessage.getIsRead());
        assertEquals("John Doe", firstMessage.getMyName());
        
        // Vérifier le deuxième message
        MessageResponseDTO secondMessage = result.get(1);
        assertEquals("Jane Smith", secondMessage.getSenderName());
        assertEquals("Deuxième message", secondMessage.getContent());
        assertEquals(false, secondMessage.getIsRead());
        assertEquals("John Doe", secondMessage.getMyName());
        
        // Vérifier le troisième message
        MessageResponseDTO thirdMessage = result.get(2);
        assertEquals("John Doe", thirdMessage.getSenderName());
        assertEquals("Troisième message", thirdMessage.getContent());
        assertEquals(true, thirdMessage.getIsRead());
        assertEquals("John Doe", thirdMessage.getMyName());
    }

    @Test
    void findMessagesDTOByConversationId_shouldReturnEmptyList_whenNoMessages() {
        // Créer une conversation sans messages
        Conversation emptyConversation = new Conversation();
        emptyConversation.setUser1(sender);
        emptyConversation.setUser2(receiver);
        emptyConversation = entityManager.persistAndFlush(emptyConversation);

        List<MessageResponseDTO> result = messageRepository.findMessagesDTOByConversationId(emptyConversation.getId(), "John Doe");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void markMessagesAsRead_shouldMarkUnreadMessagesAsRead() {
        // Vérifier l'état initial
        List<Message> initialMessages = messageRepository.findByConversationIdOrderBySentAtAsc(conversation.getId());
        long unreadCount = initialMessages.stream().filter(m -> !m.isRead()).count();
        assertEquals(2, unreadCount); // message1 et message2 sont non lus

        // Marquer comme lus les messages non lus envoyés par l'autre utilisateur
        int updatedCount = messageRepository.markMessagesAsRead(conversation.getId(), sender.getId());

        assertEquals(1, updatedCount); // Seul message2 (envoyé par receiver) devrait être marqué comme lu
        
        // Vérifier que les messages ont été mis à jour
        entityManager.clear(); // Vider le cache pour recharger depuis la DB
        List<Message> updatedMessages = messageRepository.findByConversationIdOrderBySentAtAsc(conversation.getId());
        
        // message1 (envoyé par sender) devrait rester non lu
        Message message1Updated = updatedMessages.stream()
                .filter(m -> m.getId().equals(message1.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(message1Updated);
        assertFalse(message1Updated.isRead());
        
        // message2 (envoyé par receiver) devrait être marqué comme lu
        Message message2Updated = updatedMessages.stream()
                .filter(m -> m.getId().equals(message2.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(message2Updated);
        assertTrue(message2Updated.isRead());
        
        // message3 (déjà lu) devrait rester lu
        Message message3Updated = updatedMessages.stream()
                .filter(m -> m.getId().equals(message3.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(message3Updated);
        assertTrue(message3Updated.isRead());
    }

    @Test
    void markMessagesAsRead_shouldReturnZero_whenNoUnreadMessages() {
        // Marquer tous les messages comme lus
        messageRepository.markMessagesAsRead(conversation.getId(), sender.getId());
        entityManager.clear();

        // Essayer de marquer à nouveau comme lus
        int updatedCount = messageRepository.markMessagesAsRead(conversation.getId(), sender.getId());

        
        assertEquals(0, updatedCount);
    }

    @Test
    void markMessagesAsRead_shouldReturnZero_whenNoMessages() {
        // Créer une conversation sans messages
        Conversation emptyConversation = new Conversation();
        emptyConversation.setUser1(sender);
        emptyConversation.setUser2(receiver);
        emptyConversation = entityManager.persistAndFlush(emptyConversation);

        int updatedCount = messageRepository.markMessagesAsRead(emptyConversation.getId(), sender.getId());

        assertEquals(0, updatedCount);
    }

    @Test
    void findMessagesDTOByConversationId_shouldOrderBySentAtAsc() {
        List<MessageResponseDTO> result = messageRepository.findMessagesDTOByConversationId(conversation.getId(), "John Doe");

        assertNotNull(result);
        assertEquals(3, result.size());
        
        assertTrue(result.get(0).getSentAt().isBefore(result.get(1).getSentAt()));
        assertTrue(result.get(1).getSentAt().isBefore(result.get(2).getSentAt()));
    }
}
