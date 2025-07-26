package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return l'avis créé
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        
        // S'assurer que l'utilisateur authentifié est bien le reviewer
        reviewDTO.setReviewerId(userId);

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) {

        ReviewDTO review = reviewService.getReviewById(reviewId);

        return ResponseEntity.ok(review);
    }

    /**
     * Reviews écrites par le user
     * 
     * @param userId l'id du user
     * @return liste des avis
     */
    @GetMapping("/writer/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReviewDTO>> getReviewsWrittenByUser(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.getReviewsWrittenByUser(userId);
        
        return ResponseEntity.ok(reviews);
    }

    /**
     * Reviews laissées par otherUserId sur userId
     * 
     * @param otherUserId l'id du user qui a laissé l'avis
     * @param userId l'id du user noté
     * @return liste des avis
     */
    @GetMapping("/from/{otherUserId}/to/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReviewDTO>> getReviewsFromUserToUser(@PathVariable Long otherUserId, @PathVariable Long userId) {
        
        List<ReviewDTO> reviews = reviewService.getReviewsFromUserToUser(otherUserId, userId);
        
        return ResponseEntity.ok(reviews);
    }
}