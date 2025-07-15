package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA pour l'entité Conversation.
 * Fournit des méthodes pour rechercher des conversations selon les utilisateurs.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    /**
     * Recherche toutes les conversations où l'utilisateur est user1 ou user2.
     * @param user1Id l'identifiant du premier utilisateur
     * @param user2Id l'identifiant du second utilisateur
     * @return la liste des conversations
     */
    List<Conversation> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);

    
    /**
     * Recherche une conversation entre deux utilisateurs (user1, user2).
     * @param user1Id l'identifiant du premier utilisateur
     * @param user2Id l'identifiant du second utilisateur
     * @return la conversation si elle existe
     */
    Optional<Conversation> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);


    /**
     * Recherche une conversation entre deux utilisateurs (user2, user1).
     * @param user2Id l'identifiant du second utilisateur
     * @param user1Id l'identifiant du premier utilisateur
     * @return la conversation si elle existe
     */
    Optional<Conversation> findByUser2IdAndUser1Id(Long user2Id, Long user1Id);
} 