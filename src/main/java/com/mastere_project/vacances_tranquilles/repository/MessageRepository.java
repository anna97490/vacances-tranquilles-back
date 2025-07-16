package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
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
} 