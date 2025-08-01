package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des avis entre utilisateurs.
 * Fournit des endpoints pour créer et consulter les avis.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Créer un avis sur un utilisateur (note 1-5, commentaire facultatif).
     * 
     * @param reviewDTO les infos de l'avis (note, commentaire, reservationId,
     *                  reviewerId, reviewedId)
     * @return l'avis créé
     */
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO created = reviewService.createReview(reviewDTO);

        return ResponseEntity.ok(created);
    }

    /**
     * Voir un avis précis.
     * 
     * @param reviewId l'id de l'avis
     * @return l'avis
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) {

        ReviewDTO review = reviewService.getReviewById(reviewId);

        return ResponseEntity.ok(review);
    }

    /**
     * Reviews écrites par l'utilisateur authentifié
     * 
     * @return liste des avis
     */
    @GetMapping("/writer")
    public ResponseEntity<List<ReviewDTO>> getReviewsWrittenByUser() {
        List<ReviewDTO> reviews = reviewService.getReviewsWrittenByUser();
        
        return ResponseEntity.ok(reviews);
    }

    /**
     * Reviews reçues par l'utilisateur authentifié
     * 
     * @return liste des avis
     */
    @GetMapping("/received")
    public ResponseEntity<List<ReviewDTO>> getReviewsReceivedByUser() {
        List<ReviewDTO> reviews = reviewService.getReviewsReceivedByUser();
        
        return ResponseEntity.ok(reviews);
    }
}