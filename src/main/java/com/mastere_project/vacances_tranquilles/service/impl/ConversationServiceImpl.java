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
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ConversationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.service.ConversationService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service d'implémentation pour la gestion des conversations entre utilisateurs.
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private static final String CURRENT_USER_NOT_FOUND_MESSAGE = "Current user not found: ";

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ConversationMapper conversationMapper;

    public ConversationServiceImpl(
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            ReservationRepository reservationRepository,
            ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.conversationMapper = conversationMapper;
    }

    @Override
    public List<ConversationDTO> getConversationsForUser() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérifier que l'utilisateur connecté existe en base et récupérer ses informations
        userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE));
        
        List<Conversation> conversations = conversationRepository.findByUser1IdOrUser2Id(currentUserId, currentUserId);
        
        return conversations.stream().map(conversationMapper::toDto).toList();
    }

    @Override
    public ConversationDTO createConversation(Long otherUserId, Long reservationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérifier que l'utilisateur connecté existe en base
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE + currentUserId));
        
        if (currentUserId.equals(otherUserId)) {
            throw new ConversationForbiddenException("Cannot create a conversation with yourself");
        }

        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Vérifier que la réservation existe et a le bon statut
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        
        // Vérifier que le statut de la réservation permet la création d'une conversation
        if (!"in_progress".equalsIgnoreCase(reservation.getStatus())) {
            throw new ConversationForbiddenException("Cannot create a conversation for a reservation with status: " + reservation.getStatus());
        }

        // Vérifier que les rôles sont compatibles pour une conversation
        validateConversationRoles(currentUser, otherUser);

        // Vérifier qu'une conversation n'existe pas déjà entre ces utilisateurs
        // Vérifier dans les deux sens : currentUser -> otherUser ET otherUser -> currentUser
        boolean conversationExists = conversationRepository.findByUser1IdAndUser2Id(currentUserId, otherUserId).isPresent()
                || conversationRepository.findByUser1IdAndUser2Id(otherUserId, currentUserId).isPresent()
                || conversationRepository.findByUser2IdAndUser1Id(currentUserId, otherUserId).isPresent()
                || conversationRepository.findByUser2IdAndUser1Id(otherUserId, currentUserId).isPresent();
        
        if (conversationExists) {
            throw new ConversationAlreadyExistsException("Conversation already exists between these users");
        }

        Conversation conversation = new Conversation();
        conversation.setUser1(currentUser);
        conversation.setUser2(otherUser);
        conversation.setCreatedAt(LocalDateTime.now());
        Conversation saved = conversationRepository.save(conversation);

        return conversationMapper.toDto(saved);
    }

    @Override
    public ConversationDTO getConversationById(Long conversationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérifier que l'utilisateur connecté existe en base et récupérer ses informations
        userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE));
        
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isEmpty()) {
            throw new ConversationNotFoundException("Conversation not found: " + conversationId);
        }

        Conversation conv = conversation.get();
        if (!(conv.getUser1().getId().equals(currentUserId) || conv.getUser2().getId().equals(currentUserId))) {
            throw new ConversationForbiddenException("User is not a participant in this conversation");
        }

        return conversationMapper.toDto(conv);
    }

    /**
     * Valide que les rôles des utilisateurs sont compatibles pour créer une conversation.
     * Une conversation ne peut être créée qu'entre un CLIENT et un PROVIDER.
     *
     * @param user1 le premier utilisateur
     * @param user2 le second utilisateur
     * @throws ConversationForbiddenException si les rôles ne sont pas compatibles
     */
    private void validateConversationRoles(User user1, User user2) {
        UserRole role1 = user1.getUserRole();
        UserRole role2 = user2.getUserRole();
        
        // Une conversation ne peut être créée qu'entre un CLIENT et un PROVIDER
        boolean isValidCombination = (role1 == UserRole.CLIENT && role2 == UserRole.PROVIDER) ||
                                   (role1 == UserRole.PROVIDER && role2 == UserRole.CLIENT);
        
        if (!isValidCombination) {
            throw new ConversationForbiddenException(
                "A conversation can only be created between a client and a provider. " +
                "Current roles: " + role1 + " and " + role2);
        }
    }
}
