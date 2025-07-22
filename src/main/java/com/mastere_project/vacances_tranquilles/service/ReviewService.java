package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import java.util.List;

/**
 * Interface définissant les opérations métiers pour la gestion des avis.
 */
public interface ReviewService {

    /**
     * Crée un nouvel avis à partir d'un DTO.
     *
     * @param dto les données de l'avis à créer
     * @return le DTO de l'avis créé
     */
    ReviewDTO createReview(ReviewDTO dto);

    /**
     * Récupère tous les avis reçus par un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur évalué
     * @return la liste des avis reçus
     */
    List<ReviewDTO> getReviewsForUser(Long userId);

    /**
     * Récupère un avis à partir de son identifiant.
     *
     * @param reviewId l'identifiant de l'avis recherché
     * @return le DTO de l'avis correspondant
     */
    ReviewDTO getReviewById(Long reviewId);

    /**
     * Récupère tous les avis rédigés par un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur rédacteur
     * @return la liste des avis rédigés
     */
    List<ReviewDTO> getReviewsWrittenByUser(Long userId);

    /**
     * Récupère les avis rédigés par un utilisateur spécifique à destination d’un autre utilisateur.
     *
     * @param reviewerId l'identifiant de l'évaluateur
     * @param reviewedId l'identifiant de l'évalué
     * @return la liste des avis correspondants
     */
    List<ReviewDTO> getReviewsFromUserToUser(Long reviewerId, Long reviewedId);
}
