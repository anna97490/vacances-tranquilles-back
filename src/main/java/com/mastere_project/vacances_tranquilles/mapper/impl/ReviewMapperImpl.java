package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.mapper.ReviewMapper;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Classe qui permet de convertir un objet Review en ReviewDTO et inversement.
 */
@Component
public class ReviewMapperImpl implements ReviewMapper {

    private final UserRepository userRepository;

    /**
     * Constructeur avec injection du repository utilisateur.
     *
     * @param userRepository le repository permettant de récupérer les utilisateurs
     */
    public ReviewMapperImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Convertit un objet Review en ReviewDTO.
     *
     * @param review l'objet Review à convertir
     * @return l'objet ReviewDTO correspondant, ou null si l'entrée est null
     */
    @Override
    public ReviewDTO toDTO(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setNote(review.getNote());
        dto.setCommentaire(review.getCommentaire());
        dto.setReservationId(review.getReservationId());
        dto.setReviewerId(review.getReviewer() != null ? review.getReviewer().getId() : null);
        dto.setReviewedId(review.getReviewed() != null ? review.getReviewed().getId() : null);
        dto.setCreatedAt(review.getCreatedAt());
        
        return dto;
    }

    /**
     * Convertit un objet ReviewDTO en Review.
     *
     * @param dto l'objet ReviewDTO à convertir
     * @return l'objet Review correspondant, ou null si le DTO est null
     */
    @Override
    public Review toEntity(ReviewDTO dto) {
        if (dto == null) {
            return null;
        }

        Review review = new Review();
        review.setId(dto.getId());
        review.setNote(dto.getNote());
        review.setCommentaire(dto.getCommentaire());
        review.setReservationId(dto.getReservationId());

        if (dto.getReviewerId() != null) {
            userRepository.findById(dto.getReviewerId()).ifPresent(review::setReviewer);
        }

        if (dto.getReviewedId() != null) {
            userRepository.findById(dto.getReviewedId()).ifPresent(review::setReviewed);
        }

        review.setCreatedAt(dto.getCreatedAt());
        
        return review;
    }
}
