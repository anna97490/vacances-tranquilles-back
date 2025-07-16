package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import java.util.List;

/**
 * Service pour la gestion des conversations entre utilisateurs.
 */
public interface ConversationService {

    /**
     * Récupère toutes les conversations d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des conversations de l'utilisateur
     */
    List<ConversationDTO> getConversationsForUser(Long userId);


    /**
     * Crée une nouvelle conversation entre deux utilisateurs.
     *
     * @param creatorId l'identifiant du créateur de la conversation
     * @param otherUserId l'identifiant de l'autre utilisateur
     * @return la conversation créée sous forme de DTO
     */
    ConversationDTO createConversation(Long creatorId, Long otherUserId);

    
    /**
     * Récupère une conversation par son ID si l'utilisateur y participe.
     *
     * @param conversationId l'identifiant de la conversation
     * @param userId l'identifiant de l'utilisateur
     * @return la conversation correspondante sous forme de DTO
     */
    ConversationDTO getConversationById(Long conversationId, Long userId);
} 