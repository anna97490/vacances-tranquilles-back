package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto;
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
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
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
 * Service d'implémentation pour la gestion des conversations entre
 * utilisateurs.
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

    /**
     * Récupère toutes les conversations de l'utilisateur connecté.
     * Vérifie que l'utilisateur existe en base avant de récupérer ses
     * conversations.
     *
     * @return la liste des résumés de conversations de l'utilisateur
     * @throws UserNotFoundException si l'utilisateur connecté n'existe pas en base
     */
    @Override
    public List<ConversationSummaryDto> getConversationsForUser() {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur connecté existe en base et récupérer ses
        // informations
        userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE));

        return conversationRepository.findConversationsForUser(currentUserId);
    }

    /**
     * Crée une nouvelle conversation entre l'utilisateur connecté et un autre
     * utilisateur
     * pour une réservation spécifique. Effectue plusieurs validations avant la
     * création.
     *
     * @param otherUserId   l'identifiant de l'autre utilisateur
     * @param reservationId l'identifiant de la réservation
     * @return le DTO de la conversation créée
     * @throws UserNotFoundException              si l'un des utilisateurs n'existe
     *                                            pas
     * @throws ReservationNotFoundException       si la réservation n'existe pas
     * @throws ConversationForbiddenException     si la création de conversation
     *                                            n'est pas autorisée
     * @throws ConversationAlreadyExistsException si une conversation existe déjà
     *                                            pour cette réservation
     */
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

        // Vérifier que le statut de la réservation permet la création d'une
        // conversation
        if (reservation.getStatus() != ReservationStatus.IN_PROGRESS) {
            throw new ConversationForbiddenException(
                    "Cannot create a conversation for a reservation with status: " + reservation.getStatus());
        }

        // Vérifier que les rôles sont compatibles pour une conversation
        validateConversationRoles(currentUser, otherUser);

        // Vérifier qu'une conversation n'existe pas déjà pour cette réservation
        if (reservation.getConversation() != null) {
            throw new ConversationAlreadyExistsException(
                    "A conversation already exists for reservation: " + reservationId);
        }

        Conversation conversation = new Conversation();
        conversation.setUser1(currentUser);
        conversation.setUser2(otherUser);
        conversation.setCreatedAt(LocalDateTime.now());
        Conversation saved = conversationRepository.save(conversation);

        return conversationMapper.toDto(saved);
    }

    /**
     * Récupère une conversation par son identifiant.
     * Vérifie que l'utilisateur connecté est participant de cette conversation.
     *
     * @param conversationId l'identifiant de la conversation
     * @return le DTO de la conversation
     * @throws UserNotFoundException          si l'utilisateur connecté n'existe pas
     * @throws ConversationNotFoundException  si la conversation n'existe pas
     * @throws ConversationForbiddenException si l'utilisateur n'est pas participant
     *                                        de la conversation
     */
    @Override
    public ConversationDTO getConversationById(Long conversationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur connecté existe en base et récupérer ses
        // informations
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
     * Valide que les rôles des utilisateurs sont compatibles pour créer une
     * conversation.
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