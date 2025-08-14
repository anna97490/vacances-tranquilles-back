package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;
import com.mastere_project.vacances_tranquilles.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository Spring Data JPA pour l'entité Message.
 * Fournit des méthodes pour rechercher les messages d'une conversation.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * Recherche tous les messages d'une conversation, triés par date d'envoi croissante.
     *
     * @param conversationId l'identifiant de la conversation
     * @return la liste des messages triés par date d'envoi croissante
     */
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    /**
     * Recherche tous les messages d'une conversation et les retourne sous forme de DTO.
     * Chaque message contient le nom de l'expéditeur, le contenu, la date d'envoi,
     * l'état de lecture et le nom de l'utilisateur courant.
     *
     * @param conversationId l'identifiant de la conversation
     * @param myName le nom de l'utilisateur courant
     * @return la liste des messages sous forme de DTO triés par date d'envoi croissante
     */
    @Query("SELECT new com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO(" +
       "m.id, " +
       "concat(m.sender.firstName, ' ', m.sender.lastName), " +
       "m.content, " +
       "m.sentAt, " +
       "m.read, " +
       ":myName) " + 
       "FROM Message m " +
       "WHERE m.conversation.id = :conversationId " +
       "ORDER BY m.sentAt ASC")
    List<MessageResponseDTO> findMessagesDTOByConversationId(
            @Param("conversationId") Long conversationId,
            @Param("myName") String myName
    );

    /**
     * Marque tous les messages non lus d'une conversation comme lus,
     * à l'exception de ceux envoyés par l'utilisateur spécifié.
     *
     * @param conversationId l'identifiant de la conversation
     * @param userId l'identifiant de l'utilisateur dont les messages ne doivent pas être marqués comme lus
     * @return le nombre de messages marqués comme lus
     */
    @Modifying
    @Query("UPDATE Message m SET m.read = true " +
        "WHERE m.conversation.id = :conversationId " +
        "AND m.read = false " +
        "AND m.sender.id <> :userId")
    int markMessagesAsRead(
        @Param("conversationId") Long conversationId,
        @Param("userId") Long userId
    );
} 
