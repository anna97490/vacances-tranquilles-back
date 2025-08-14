package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * Recherche toutes les conversations d'un utilisateur à travers ses réservations.
     * Retourne un résumé des conversations avec les informations de l'autre participant,
     * le service associé et les dates de réservation.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des résumés de conversations triés par date de réservation décroissante
     */
    @Query("""
    SELECT new com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto(
        r.conversation.id,
        CASE
            WHEN r.client.id = :userId THEN CONCAT(r.provider.firstName, ' ', r.provider.lastName)
            ELSE CONCAT(r.client.firstName, ' ', r.client.lastName)
        END,
        r.service.title,
        r.reservationDate,
        r.startDate
    )
    FROM Reservation r
    WHERE (r.client.id = :userId OR r.provider.id = :userId)
    AND r.conversation IS NOT NULL
    ORDER BY r.reservationDate DESC, r.startDate DESC
    """)
    List<ConversationSummaryDto> findConversationsForUser(@Param("userId") Long userId);


    /**
     * Recherche toutes les conversations où l'utilisateur est user1 ou user2.
     *
     * @param user1Id l'identifiant du premier utilisateur
     * @param user2Id l'identifiant du second utilisateur
     * @return la liste des conversations trouvées
     */
    List<Conversation> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);

    
    /**
     * Recherche une conversation entre deux utilisateurs (user1, user2).
     *
     * @param user1Id l'identifiant du premier utilisateur
     * @param user2Id l'identifiant du second utilisateur
     * @return un Optional contenant la conversation si elle existe, sinon vide
     */
    Optional<Conversation> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);


    /**
     * Recherche une conversation entre deux utilisateurs (user2, user1).
     *
     * @param user2Id l'identifiant du second utilisateur
     * @param user1Id l'identifiant du premier utilisateur
     * @return un Optional contenant la conversation si elle existe, sinon vide
     */
    Optional<Conversation> findByUser2IdAndUser1Id(Long user2Id, Long user1Id);
} 
