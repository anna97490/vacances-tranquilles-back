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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service d'implémentation pour la gestion des messages dans les conversations.
 */
@Service
public class MessageServiceImpl implements MessageService {

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
    public List<MessageDTO> getMessagesByConversationId(Long conversationId, Long userId) {
        List<Message> messages = messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
        boolean updated = false;

        for (Message msg : messages) {
            if (!msg.isRead() && !msg.getSender().getId().equals(userId)) {
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
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Message message = messageMapper.toEntity(messageDTO);

        Conversation conversation = conversationRepository.findById(messageDTO.getConversationId())
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found: " + messageDTO.getConversationId()));

        Long senderId = messageDTO.getSenderId();
        if (!(conversation.getUser1().getId().equals(senderId) || conversation.getUser2().getId().equals(senderId))) {
            throw new ConversationForbiddenException("Sender is not a participant of the conversation");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("Sender not found: " + senderId));

        message.setConversation(conversation);
        message.setSender(sender);
        message.setRead(false);
        message.setSentAt(message.getSentAt() == null ? LocalDateTime.now() : message.getSentAt());

        return messageMapper.toDto(messageRepository.save(message));
    }

    @Override
    public MessageDTO updateMessage(Long id, MessageDTO messageDTO, Long userId) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ConversationNotFoundException("Message not found: " + id));

        if (!message.getSender().getId().equals(userId)) {
            throw new ConversationForbiddenException("User is not the sender of the message");
        }

        message.setContent(messageDTO.getContent());
        return messageMapper.toDto(messageRepository.save(message));
    }
}
