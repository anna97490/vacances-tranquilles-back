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
import com.mastere_project.vacances_tranquilles.service.MessageService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public List<MessageDTO> getMessagesByConversationId(Long conversationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérifier que l'utilisateur connecté existe en base
        userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE + currentUserId));
        
        // Vérifier que la conversation existe
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found: " + conversationId));
        
        // Vérifier que l'utilisateur connecté est participant de cette conversation
        if (!(conversation.getUser1().getId().equals(currentUserId) || conversation.getUser2().getId().equals(currentUserId))) {
            throw new ConversationForbiddenException("User is not a participant in this conversation");
        }
        
        List<Message> messages = messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
        boolean updated = false;

        for (Message msg : messages) {
            if (!msg.isRead() && !msg.getSender().getId().equals(currentUserId)) {
                msg.setRead(true);
                updated = true;
            }
        }

        if (updated) {
            messageRepository.saveAll(messages);
        }

        return messages.stream().map(messageMapper::toDto).toList();
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérifier que l'utilisateur connecté existe en base
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE + currentUserId));
        
        Message message = messageMapper.toEntity(messageDTO);

        Conversation conversation = conversationRepository.findById(messageDTO.getConversationId())
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found: " + messageDTO.getConversationId()));

        if (!(conversation.getUser1().getId().equals(currentUserId) || conversation.getUser2().getId().equals(currentUserId))) {
            throw new ConversationForbiddenException("Sender is not a participant in this conversation");
        }

        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setRead(false);
        message.setSentAt(message.getSentAt() == null ? LocalDateTime.now() : message.getSentAt());

        return messageMapper.toDto(messageRepository.save(message));
    }

    @Override
    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérifier que l'utilisateur connecté existe en base
        userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE));
        
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ConversationNotFoundException("Message not found: " + id));

        if (!message.getSender().getId().equals(currentUserId)) {
            throw new ConversationForbiddenException("User is not the message sender");
        }

        message.setContent(messageDTO.getContent());
        return messageMapper.toDto(messageRepository.save(message));
    }
}
