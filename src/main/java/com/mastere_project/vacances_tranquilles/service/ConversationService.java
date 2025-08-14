package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;

import java.util.List;

/**
 * Service pour la gestion des conversations entre utilisateurs.
 */
public interface ConversationService {

    /**
     * Récupère toutes les conversations de l'utilisateur connecté.
     *
     * @return la liste des conversations de l'utilisateur connecté
     */
    List<ConversationSummaryDto> getConversationsForUser();

    /**
     * Crée une nouvelle conversation entre l'utilisateur connecté et l'autre participant de la réservation.
     *
     * @param reservationId l'identifiant de la réservation liée à cette conversation
     * @return la conversation créée sous forme de DTO
     */
    ConversationDTO createConversation(Long reservationId);

    /**
     * Récupère une conversation par son ID si l'utilisateur connecté y participe.
     *
     * @param conversationId l'identifiant de la conversation
     * @return la conversation correspondante sous forme de DTO
     */
    ConversationDTO getConversationById(Long conversationId);

    /**
     * Récupère les informations de la réservation associée à une conversation.
     *
     * @param conversationId l'identifiant de la conversation
     * @return la réservation associée sous forme de DTO
     */
    ReservationResponseDTO getReservationByConversationId(Long conversationId);
}