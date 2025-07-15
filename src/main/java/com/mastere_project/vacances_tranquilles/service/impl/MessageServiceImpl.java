package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.Message;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.mapper.MessageMapper;
import com.mastere_project.vacances_tranquilles.repository.ConversationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.repository.MessageRepository;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import org.springframework.stereotype.Service;

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

    public MessageServiceImpl(
            MessageRepository messageRepository,
            MessageMapper messageMapper,
            ConversationRepository conversationRepository,
            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Récupère tous les messages d'une conversation et marque comme lus ceux envoyés par l'autre utilisateur.
     * @param conversationId l'identifiant de la conversation
     * @param userId l'identifiant de l'utilisateur courant
     * @return la liste des messages
     */
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


    /**
     * Récupère tous les messages du système.
     * @return la liste de tous les messages
     */
    @Override
    public List<MessageDTO> getAllMessages() {

        List<Message> messages = messageRepository.findAll();

        return messages.stream().map(messageMapper::toDto).toList();
    }


    /**
     * Envoie un nouveau message dans une conversation.
     * @param messageDTO le message à envoyer
     * @return le message sauvegardé
     */
    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {

        Message message = messageMapper.toEntity(messageDTO);
        // Set conversation
        Conversation conversation = conversationRepository.findById(messageDTO.getConversationId())
                .orElseThrow(() -> new com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException("Conversation not found: " + messageDTO.getConversationId()));
        // Vérifie que le sender fait bien partie de la conversation
        Long senderId = messageDTO.getSenderId();

        if (!(conversation.getUser1().getId().equals(senderId) || conversation.getUser2().getId().equals(senderId))) {
            throw new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException("Sender is not a participant of the conversation");
        }

        message.setConversation(conversation);

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new com.mastere_project.vacances_tranquilles.exception.UserNotFoundException("Sender not found: " + senderId));
        message.setSender(sender);

        if (message.getSentAt() == null) {
            message.setSentAt(java.time.LocalDateTime.now());
        }

        message.setRead(false);
        Message saved = messageRepository.save(message);

        return messageMapper.toDto(saved);
    }


    /**
     * Modifie un message existant (seul l'auteur peut modifier).
     * @param id l'identifiant du message
     * @param messageDTO le contenu modifié
     * @param userId l'identifiant de l'utilisateur courant
     * @return le message modifié
     */
    @Override
    public MessageDTO updateMessage(Long id, MessageDTO messageDTO, Long userId) {

        Message message = messageRepository.findById(id).orElse(null);

        if (message == null) {
            throw new com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException("Message not found: " + id);
        }

        if (!message.getSender().getId().equals(userId)) {
            throw new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException("User is not the sender of the message");
        }

        message.setContent(messageDTO.getContent());
        Message saved = messageRepository.save(message);

        return messageMapper.toDto(saved);
    }
}