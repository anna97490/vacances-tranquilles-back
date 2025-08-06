package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository Spring Data JPA pour l'entité Review.
 * Fournit des méthodes de recherche de commentaires selon différents critères.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Recherche toutes les évaluations reçues par un utilisateur spécifique.
     *
     * @param reviewedId l'identifiant de l'utilisateur évalué
     * @return la liste des évaluations reçues
     */
    List<Review> findByReviewedId(Long reviewedId);

    /**
     * Recherche toutes les évaluations rédigées par un utilisateur spécifique.
     *
     * @param reviewerId l'identifiant de l'utilisateur ayant rédigé les évaluations
     * @return la liste des évaluations rédigées
     */
    List<Review> findByReviewerId(Long reviewerId);

    /**
     * Recherche les évaluations qu’un utilisateur a données à un autre utilisateur spécifique.
     *
     * @param reviewerId l’identifiant de l’évaluateur
     * @param reviewedId l’identifiant de l’évalué
     * @return la liste des évaluations correspondantes
     */
    List<Review> findByReviewerIdAndReviewedId(Long reviewerId, Long reviewedId);
}