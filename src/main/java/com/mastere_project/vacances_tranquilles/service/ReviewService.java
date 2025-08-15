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
     * Récupère un avis à partir de son identifiant.
     *
     * @param reviewId l'identifiant de l'avis recherché
     * @return le DTO de l'avis correspondant
     */
    ReviewDTO getReviewById(Long reviewId);

    /**
     * Récupère tous les avis rédigés par l'utilisateur authentifié.
     *
     * @return la liste des avis rédigés
     */
    List<ReviewDTO> getReviewsWrittenByUser();

    /**
     * Récupère tous les avis reçus par l'utilisateur authentifié.
     *
     * @return la liste des avis reçus
     */
    List<ReviewDTO> getReviewsReceivedByUser();
}