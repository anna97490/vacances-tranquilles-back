package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
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
    List<ConversationDTO> getConversationsForUser();

    /**
     * Crée une nouvelle conversation entre l'utilisateur connecté et un autre utilisateur pour une réservation spécifique.
     *
     * @param otherUserId l'identifiant de l'autre utilisateur
     * @param reservationId l'identifiant de la réservation liée à cette conversation
     * @return la conversation créée sous forme de DTO
     */
    ConversationDTO createConversation(Long otherUserId, Long reservationId);

    /**
     * Récupère une conversation par son ID si l'utilisateur connecté y participe.
     *
     * @param conversationId l'identifiant de la conversation
     * @return la conversation correspondante sous forme de DTO
     */
    ConversationDTO getConversationById(Long conversationId);
}
