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
import com.mastere_project.vacances_tranquilles.service.ConversationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service d'implémentation pour la gestion des conversations entre utilisateurs.
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;

    public ConversationServiceImpl(
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.conversationMapper = conversationMapper;
    }

    @Override
    public List<ConversationDTO> getConversationsForUser(Long userId) {
        List<Conversation> conversations = conversationRepository.findByUser1IdOrUser2Id(userId, userId);
        return conversations.stream().map(conversationMapper::toDto).toList();
    }

    @Override
    public ConversationDTO createConversation(Long creatorId, Long otherUserId) {
        if (creatorId.equals(otherUserId)) {
            throw new ConversationForbiddenException("Impossible de créer une conversation avec soi-même");
        }

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + creatorId));
        User other = userRepository.findById(otherUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + otherUserId));

        if (conversationRepository.findByUser1IdAndUser2Id(creatorId, otherUserId).isPresent()
                || conversationRepository.findByUser2IdAndUser1Id(creatorId, otherUserId).isPresent()) {
            throw new ConversationAlreadyExistsException("Conversation already exists between these users");
        }

        Conversation conversation = new Conversation();
        conversation.setUser1(creator);
        conversation.setUser2(other);
        conversation.setCreatedAt(LocalDateTime.now());
        Conversation saved = conversationRepository.save(conversation);

        return conversationMapper.toDto(saved);
    }

    @Override
    public ConversationDTO getConversationById(Long conversationId, Long userId) {
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isEmpty()) {
            throw new ConversationNotFoundException("Conversation not found: " + conversationId);
        }

        Conversation conv = conversation.get();
        if (!(conv.getUser1().getId().equals(userId) || conv.getUser2().getId().equals(userId))) {
            throw new ConversationForbiddenException("User is not a participant of the conversation");
        }

        return conversationMapper.toDto(conv);
    }
}
