package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;

import java.util.List;

/**
 * Service pour la gestion des messages dans les conversations.
 */
public interface MessageService {

    /**
     * Récupère tous les messages d'une conversation et marque comme lus ceux envoyés par l'autre utilisateur.
     *
     * @param conversationId l'identifiant de la conversation
     * @return la liste des messages de la conversation
     */
    List<MessageResponseDTO> getMessagesByConversationId(Long conversationId);

    /**
     * Envoie un nouveau message dans une conversation.
     *
     * @param messageDTO le message à envoyer
     * @return le message sauvegardé sous forme de DTO
     */
    MessageDTO sendMessage(MessageDTO messageDTO);

    /**
     * Modifie un message existant (seul l'auteur peut modifier).
     *
     * @param id l'identifiant du message à modifier
     * @param messageDTO le contenu modifié du message
     * @return le message modifié sous forme de DTO
     */
    MessageDTO updateMessage(Long id, MessageDTO messageDTO);
}