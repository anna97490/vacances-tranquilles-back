package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.repository.ConversationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.ConversationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service d'implémentation pour la gestion des conversations entre utilisateurs.
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final com.mastere_project.vacances_tranquilles.mapper.ConversationMapper conversationMapper;

    /**
     * Constructeur du service ConversationServiceImpl.
     * @param conversationRepository le repository des conversations
     * @param userRepository le repository des utilisateurs
     * @param conversationMapper le mapper de conversation
     */
    public ConversationServiceImpl(
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            com.mastere_project.vacances_tranquilles.mapper.ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.conversationMapper = conversationMapper;
    }

    
    /**
     * Récupère toutes les conversations d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des conversations de l'utilisateur
     */
    @Override
    public List<ConversationDTO> getConversationsForUser(Long userId) {

        List<Conversation> conversations = conversationRepository.findByUser1IdOrUser2Id(userId, userId);
        return conversations.stream().map(conversationMapper::toDto).toList();
    }


    /**
     * Crée une nouvelle conversation entre deux utilisateurs.
     *
     * @param creatorId l'identifiant du créateur de la conversation
     * @param otherUserId l'identifiant de l'autre utilisateur
     * @return la conversation créée sous forme de DTO
     */
    @Override
    public ConversationDTO createConversation(Long creatorId, Long otherUserId) {
        // Vérifier que les deux users existent
        if (creatorId.equals(otherUserId)) {
            throw new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException(
                "Impossible de créer une conversation avec soi-même");
        }
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new com.mastere_project.vacances_tranquilles.exception.UserNotFoundException("User not found: " + creatorId));
        User other = userRepository.findById(otherUserId)
                .orElseThrow(() -> new com.mastere_project.vacances_tranquilles.exception.UserNotFoundException("User not found: " + otherUserId));

        // Vérifier si la conversation existe déjà (dans les deux sens)
        if (conversationRepository.findByUser1IdAndUser2Id(creatorId, otherUserId).isPresent() ||
                conversationRepository.findByUser2IdAndUser1Id(otherUserId, creatorId).isPresent()) {
            throw new com.mastere_project.vacances_tranquilles.exception.ConversationAlreadyExistsException(
                    "Conversation already exists between these users");
        }

        Conversation conversation = new Conversation();
        conversation.setUser1(creator);
        conversation.setUser2(other);
        conversation.setCreatedAt(java.time.LocalDateTime.now());
        Conversation saved = conversationRepository.save(conversation);

        return conversationMapper.toDto(saved);
    }


    /**
     * Récupère une conversation par son ID si l'utilisateur y participe.
     *
     * @param conversationId l'identifiant de la conversation
     * @param userId l'identifiant de l'utilisateur
     * @return la conversation correspondante sous forme de DTO
     */
    @Override
    public ConversationDTO getConversationById(Long conversationId, Long userId) {

        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isEmpty()) {
            throw new com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException("Conversation not found: " + conversationId);
        }
        Conversation conv = conversation.get();
        if (!(conv.getUser1().getId().equals(userId) || conv.getUser2().getId().equals(userId))) {
            throw new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException("User is not a participant of the conversation");
        }
        
        return conversationMapper.toDto(conv);
    }
}