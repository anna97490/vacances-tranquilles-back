package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;
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
import com.mastere_project.vacances_tranquilles.service.MessageService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service d'implémentation pour la gestion des messages dans les conversations.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final String CURRENT_USER_NOT_FOUND_MESSAGE = "Current user not found: ";

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository,
            MessageMapper messageMapper,
            ConversationRepository conversationRepository,
            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Récupère tous les messages d'une conversation et marque les messages non lus comme lus.
     * Vérifie que l'utilisateur connecté est participant de cette conversation.
     *
     * @param conversationId l'identifiant de la conversation
     * @return la liste des messages de la conversation sous forme de DTO
     * @throws UserNotFoundException          si l'utilisateur connecté n'existe pas
     * @throws ConversationNotFoundException  si la conversation n'existe pas
     * @throws ConversationForbiddenException si l'utilisateur n'est pas participant de la conversation
     */
    @Transactional
    @Override
    public List<MessageResponseDTO> getMessagesByConversationId(Long conversationId) {
        if (conversationId == null) {
            throw new IllegalArgumentException("Conversation ID cannot be null");
        }

        Long currentUserId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur connecté existe en base
        userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE + currentUserId));

        // Vérifier que la conversation existe
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found: " + conversationId));

        // Vérifier que l'utilisateur connecté est participant de cette conversation
        if (!(conversation.getUser1().getId().equals(currentUserId)
                || conversation.getUser2().getId().equals(currentUserId))) {
            throw new ConversationForbiddenException("User is not a participant in this conversation");
        }

        User whoIam = this.userRepository.getReferenceById(currentUserId);

        messageRepository.markMessagesAsRead(conversationId, currentUserId);

        return messageRepository.findMessagesDTOByConversationId(conversationId,
                whoIam.getFirstName() + " " + whoIam.getLastName());
    }

    /**
     * Envoie un nouveau message dans une conversation.
     * Vérifie que l'utilisateur connecté est participant de cette conversation.
     *
     * @param messageDTO le DTO contenant les informations du message à envoyer
     * @return le DTO du message envoyé
     * @throws UserNotFoundException          si l'utilisateur connecté n'existe pas
     * @throws ConversationNotFoundException  si la conversation n'existe pas
     * @throws ConversationForbiddenException si l'utilisateur n'est pas participant de la conversation
     */
    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        if (messageDTO == null) {
            throw new IllegalArgumentException("Message DTO cannot be null");
        }
        if (messageDTO.getConversationId() == null) {
            throw new IllegalArgumentException("Conversation ID cannot be null");
        }
        if (messageDTO.getContent() == null || messageDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }

        Long currentUserId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur connecté existe en base
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE + currentUserId));

        Message message = messageMapper.toEntity(messageDTO);

        Conversation conversation = conversationRepository.findById(messageDTO.getConversationId())
                .orElseThrow(() -> new ConversationNotFoundException(
                        "Conversation not found: " + messageDTO.getConversationId()));

        if (!(conversation.getUser1().getId().equals(currentUserId)
                || conversation.getUser2().getId().equals(currentUserId))) {
            throw new ConversationForbiddenException("Sender is not a participant in this conversation");
        }

        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setRead(false);
        message.setSentAt(message.getSentAt() == null ? LocalDateTime.now() : message.getSentAt());

        return messageMapper.toDto(messageRepository.save(message));
    }

    /**
     * Met à jour le contenu d'un message existant.
     * Vérifie que l'utilisateur connecté est l'expéditeur du message.
     *
     * @param id         l'identifiant du message à mettre à jour
     * @param messageDTO le DTO contenant le nouveau contenu du message
     * @return le DTO du message mis à jour
     * @throws UserNotFoundException          si l'utilisateur connecté n'existe pas
     * @throws ConversationNotFoundException  si le message n'existe pas
     * @throws ConversationForbiddenException si l'utilisateur n'est pas l'expéditeur du message
     */
    @Override
    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        if (id == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        if (messageDTO == null) {
            throw new IllegalArgumentException("Message DTO cannot be null");
        }
        if (messageDTO.getContent() == null || messageDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }

        Long currentUserId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur connecté existe en base
        userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE + currentUserId));

        // Récupérer le message existant
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new ConversationNotFoundException("Message not found: " + id));

        // Vérifier que l'utilisateur connecté est l'expéditeur du message
        if (!existingMessage.getSender().getId().equals(currentUserId)) {
            throw new ConversationForbiddenException("You can only update your own messages");
        }

        // Mettre à jour le contenu du message
        existingMessage.setContent(messageDTO.getContent().trim());
        existingMessage.setSentAt(LocalDateTime.now());

        return messageMapper.toDto(messageRepository.save(existingMessage));
    }
}