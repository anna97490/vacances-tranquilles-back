package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import java.util.List;

/**
 * Service pour la gestion des messages dans les conversations.
 */
public interface MessageService {

    /**
     * Récupère tous les messages d'une conversation et marque comme lus ceux envoyés par l'autre utilisateur.
     *
     * @param conversationId l'identifiant de la conversation
     * @param userId l'identifiant de l'utilisateur courant
     * @return la liste des messages de la conversation
     */
    List<MessageDTO> getMessagesByConversationId(Long conversationId, Long userId);


    /**
     * Récupère tous les messages du système.
     *
     * @return la liste de tous les messages du système
     */
    List<MessageDTO> getAllMessages();


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
     * @param userId l'identifiant de l'utilisateur courant
     * @return le message modifié sous forme de DTO
     */
    MessageDTO updateMessage(Long id, MessageDTO messageDTO, Long userId);
} 