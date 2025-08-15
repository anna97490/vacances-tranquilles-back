package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests d'intégration pour le repository ReviewRepository.
 * Utilise une base de données en mémoire pour tester les requêtes JPA.
 */
@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    private User user1;
    private User user2;
    private User user3;
    private Review review1;
    private Review review2;
    private Review review3;
    private Review review4;

    @BeforeEach
    void setUp() {
        // Création des utilisateurs de test
        user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setPassword("password1");
        user1.setFirstName("Test1");
        user1.setLastName("User1");
        user1.setPhoneNumber("0123456789");
        user1.setAddress("123 Test Street");
        user1.setCity("Test City");
        user1.setPostalCode("12345");
        user1.setUserRole(com.mastere_project.vacances_tranquilles.model.enums.UserRole.CLIENT);
        entityManager.persistAndFlush(user1);

        user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setPassword("password2");
        user2.setFirstName("Test2");
        user2.setLastName("User2");
        user2.setPhoneNumber("0987654321");
        user2.setAddress("456 Test Street");
        user2.setCity("Test City");
        user2.setPostalCode("12345");
        user2.setUserRole(com.mastere_project.vacances_tranquilles.model.enums.UserRole.PROVIDER);
        entityManager.persistAndFlush(user2);

        user3 = new User();
        user3.setEmail("user3@test.com");
        user3.setPassword("password3");
        user3.setFirstName("Test3");
        user3.setLastName("User3");
        user3.setPhoneNumber("0555666777");
        user3.setAddress("789 Test Street");
        user3.setCity("Test City");
        user3.setPostalCode("12345");
        user3.setUserRole(com.mastere_project.vacances_tranquilles.model.enums.UserRole.CLIENT);
        entityManager.persistAndFlush(user3);

        // Création des avis de test
        review1 = new Review();
        review1.setNote(5);
        review1.setCommentaire("Excellent service !");
        review1.setReservationId(1L);
        review1.setReviewer(user1);
        review1.setReviewed(user2);
        review1.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(review1);

        review2 = new Review();
        review2.setNote(4);
        review2.setCommentaire("Très bon travail");
        review2.setReservationId(2L);
        review2.setReviewer(user2);
        review2.setReviewed(user1);
        review2.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(review2);

        review3 = new Review();
        review3.setNote(3);
        review3.setCommentaire("Correct");
        review3.setReservationId(3L);
        review3.setReviewer(user1);
        review3.setReviewed(user3);
        review3.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(review3);

        review4 = new Review();
        review4.setNote(5);
        review4.setCommentaire("Parfait !");
        review4.setReservationId(4L);
        review4.setReviewer(user3);
        review4.setReviewed(user2);
        review4.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(review4);
    }

    @Test
    @DisplayName("findByReviewedId should return all reviews received by a user")
    void findByReviewedId_shouldReturnAllReviewsReceivedByUser() {
        // Test pour user2 (provider) qui a reçu 2 avis
        List<Review> reviewsReceivedByUser2 = reviewRepository.findByReviewedId(user2.getId());
        
        assertThat(reviewsReceivedByUser2).hasSize(2);
        assertThat(reviewsReceivedByUser2).extracting("reviewer.id")
                .containsExactlyInAnyOrder(user1.getId(), user3.getId());
        assertThat(reviewsReceivedByUser2).extracting("reviewed.id")
                .containsOnly(user2.getId());
    }

    @Test
    @DisplayName("findByReviewedId should return empty list when user has no reviews")
    void findByReviewedId_shouldReturnEmptyListWhenNoReviews() {
        // Créer un utilisateur sans avis
        User userWithoutReviews = new User();
        userWithoutReviews.setEmail("no-reviews@test.com");
        userWithoutReviews.setPassword("password");
        userWithoutReviews.setFirstName("User");
        userWithoutReviews.setLastName("NoReviews");
        userWithoutReviews.setPhoneNumber("0000000000");
        userWithoutReviews.setAddress("Test Address");
        userWithoutReviews.setCity("Test City");
        userWithoutReviews.setPostalCode("12345");
        userWithoutReviews.setUserRole(com.mastere_project.vacances_tranquilles.model.enums.UserRole.CLIENT);
        entityManager.persistAndFlush(userWithoutReviews);

        List<Review> reviews = reviewRepository.findByReviewedId(userWithoutReviews.getId());
        
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("findByReviewerId should return all reviews written by a user")
    void findByReviewerId_shouldReturnAllReviewsWrittenByUser() {
        // Test pour user1 qui a écrit 2 avis
        List<Review> reviewsWrittenByUser1 = reviewRepository.findByReviewerId(user1.getId());
        
        assertThat(reviewsWrittenByUser1).hasSize(2);
        assertThat(reviewsWrittenByUser1).extracting("reviewer.id")
                .containsOnly(user1.getId());
        assertThat(reviewsWrittenByUser1).extracting("reviewed.id")
                .containsExactlyInAnyOrder(user2.getId(), user3.getId());
    }

    @Test
    @DisplayName("findByReviewerId should return empty list when user has written no reviews")
    void findByReviewerId_shouldReturnEmptyListWhenNoReviewsWritten() {
        // Créer un utilisateur qui n'a pas écrit d'avis
        User userWithoutWrittenReviews = new User();
        userWithoutWrittenReviews.setEmail("no-written@test.com");
        userWithoutWrittenReviews.setPassword("password");
        userWithoutWrittenReviews.setFirstName("User");
        userWithoutWrittenReviews.setLastName("NoWritten");
        userWithoutWrittenReviews.setPhoneNumber("1111111111");
        userWithoutWrittenReviews.setAddress("Test Address");
        userWithoutWrittenReviews.setCity("Test City");
        userWithoutWrittenReviews.setPostalCode("12345");
        userWithoutWrittenReviews.setUserRole(com.mastere_project.vacances_tranquilles.model.enums.UserRole.CLIENT);
        entityManager.persistAndFlush(userWithoutWrittenReviews);

        List<Review> reviews = reviewRepository.findByReviewerId(userWithoutWrittenReviews.getId());
        
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("findByReviewerIdAndReviewedId should return reviews between specific users")
    void findByReviewerIdAndReviewedId_shouldReturnReviewsBetweenSpecificUsers() {
        // Test pour les avis de user1 vers user2
        List<Review> reviewsFromUser1ToUser2 = reviewRepository.findByReviewerIdAndReviewedId(user1.getId(), user2.getId());
        
        assertThat(reviewsFromUser1ToUser2).hasSize(1);
        assertThat(reviewsFromUser1ToUser2.get(0).getReviewer().getId()).isEqualTo(user1.getId());
        assertThat(reviewsFromUser1ToUser2.get(0).getReviewed().getId()).isEqualTo(user2.getId());
        assertThat(reviewsFromUser1ToUser2.get(0).getCommentaire()).isEqualTo("Excellent service !");
    }

    @Test
    @DisplayName("findByReviewerIdAndReviewedId should return empty list when no reviews exist between users")
    void findByReviewerIdAndReviewedId_shouldReturnEmptyListWhenNoReviewsBetweenUsers() {
        // Test pour des utilisateurs qui n'ont pas d'avis entre eux
        List<Review> reviews = reviewRepository.findByReviewerIdAndReviewedId(user1.getId(), user1.getId());
        
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("existsByReservationIdAndReviewerId should return true when review exists")
    void existsByReservationIdAndReviewerId_shouldReturnTrueWhenReviewExists() {
        // Test pour une réservation et reviewer qui existent
        boolean exists = reviewRepository.existsByReservationIdAndReviewerId(1L, user1.getId());
        
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsByReservationIdAndReviewerId should return false when review does not exist")
    void existsByReservationIdAndReviewerId_shouldReturnFalseWhenReviewDoesNotExist() {
        // Test pour une réservation qui n'existe pas
        boolean exists = reviewRepository.existsByReservationIdAndReviewerId(999L, user1.getId());
        
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsByReservationIdAndReviewerId should return false when reviewer does not exist")
    void existsByReservationIdAndReviewerId_shouldReturnFalseWhenReviewerDoesNotExist() {
        // Test pour un reviewer qui n'existe pas
        boolean exists = reviewRepository.existsByReservationIdAndReviewerId(1L, 999L);
        
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("findByReservationIdAndReviewerId should return review when it exists")
    void findByReservationIdAndReviewerId_shouldReturnReviewWhenExists() {
        // Test pour une réservation et reviewer qui existent
        Optional<Review> review = reviewRepository.findByReservationIdAndReviewerId(1L, user1.getId());
        
        assertThat(review).isPresent();
        assertThat(review.get().getReservationId()).isEqualTo(1L);
        assertThat(review.get().getReviewer().getId()).isEqualTo(user1.getId());
        assertThat(review.get().getReviewed().getId()).isEqualTo(user2.getId());
        assertThat(review.get().getCommentaire()).isEqualTo("Excellent service !");
    }

    @Test
    @DisplayName("findByReservationIdAndReviewerId should return empty when review does not exist")
    void findByReservationIdAndReviewerId_shouldReturnEmptyWhenReviewDoesNotExist() {
        // Test pour une réservation qui n'existe pas
        Optional<Review> review = reviewRepository.findByReservationIdAndReviewerId(999L, user1.getId());
        
        assertThat(review).isEmpty();
    }

    @Test
    @DisplayName("findByReservationIdAndReviewerId should return empty when reviewer does not exist")
    void findByReservationIdAndReviewerId_shouldReturnEmptyWhenReviewerDoesNotExist() {
        // Test pour un reviewer qui n'existe pas
        Optional<Review> review = reviewRepository.findByReservationIdAndReviewerId(1L, 999L);
        
        assertThat(review).isEmpty();
    }

    @Test
    @DisplayName("Repository should handle multiple reviews for same reservation by different reviewers")
    void repository_shouldHandleMultipleReviewsForSameReservationByDifferentReviewers() {
        // Créer un autre avis pour la même réservation par un autre reviewer
        Review additionalReview = new Review();
        additionalReview.setNote(4);
        additionalReview.setCommentaire("Bon service aussi");
        additionalReview.setReservationId(1L); // Même réservation que review1
        additionalReview.setReviewer(user3);
        additionalReview.setReviewed(user2);
        additionalReview.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(additionalReview);

        // Vérifier que les deux avis existent pour la même réservation
        boolean existsUser1 = reviewRepository.existsByReservationIdAndReviewerId(1L, user1.getId());
        boolean existsUser3 = reviewRepository.existsByReservationIdAndReviewerId(1L, user3.getId());
        
        assertThat(existsUser1).isTrue();
        assertThat(existsUser3).isTrue();

        // Vérifier que chaque reviewer peut récupérer son avis
        Optional<Review> reviewUser1 = reviewRepository.findByReservationIdAndReviewerId(1L, user1.getId());
        Optional<Review> reviewUser3 = reviewRepository.findByReservationIdAndReviewerId(1L, user3.getId());
        
        assertThat(reviewUser1).isPresent();
        assertThat(reviewUser3).isPresent();
        assertThat(reviewUser1.get().getReviewer().getId()).isEqualTo(user1.getId());
        assertThat(reviewUser3.get().getReviewer().getId()).isEqualTo(user3.getId());
    }

    @Test
    @DisplayName("Repository should handle reviews with null commentaire")
    void repository_shouldHandleReviewsWithNullCommentaire() {
        // Créer un avis sans commentaire
        Review reviewWithoutComment = new Review();
        reviewWithoutComment.setNote(3);
        reviewWithoutComment.setCommentaire(null);
        reviewWithoutComment.setReservationId(5L);
        reviewWithoutComment.setReviewer(user1);
        reviewWithoutComment.setReviewed(user2);
        reviewWithoutComment.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(reviewWithoutComment);

        // Vérifier que l'avis est trouvé
        Optional<Review> foundReview = reviewRepository.findByReservationIdAndReviewerId(5L, user1.getId());
        
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getCommentaire()).isNull();
        assertThat(foundReview.get().getNote()).isEqualTo(3);
    }

    @Test
    @DisplayName("Repository should handle edge case values")
    void repository_shouldHandleEdgeCaseValues() {
        // Créer un avis avec des valeurs limites
        Review edgeCaseReview = new Review();
        edgeCaseReview.setNote(1); // Note minimale
        edgeCaseReview.setCommentaire(""); // Commentaire vide
        edgeCaseReview.setReservationId(0L); // ID de réservation à 0
        edgeCaseReview.setReviewer(user1);
        edgeCaseReview.setReviewed(user2);
        edgeCaseReview.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0)); // Date ancienne
        entityManager.persistAndFlush(edgeCaseReview);

        // Vérifier que l'avis est trouvé
        Optional<Review> foundReview = reviewRepository.findByReservationIdAndReviewerId(0L, user1.getId());
        
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getNote()).isEqualTo(1);
        assertThat(foundReview.get().getCommentaire()).isEmpty();
        assertThat(foundReview.get().getReservationId()).isZero();
        assertThat(foundReview.get().getCreatedAt()).isEqualTo(LocalDateTime.of(2020, 1, 1, 0, 0));
    }

    @Test
    @DisplayName("Repository should handle very long commentaire")
    void repository_shouldHandleVeryLongCommentaire() {
        String longComment = "A".repeat(1000); // Commentaire très long
        
        Review longCommentReview = new Review();
        longCommentReview.setNote(5);
        longCommentReview.setCommentaire(longComment);
        longCommentReview.setReservationId(6L);
        longCommentReview.setReviewer(user1);
        longCommentReview.setReviewed(user2);
        longCommentReview.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(longCommentReview);

        // Vérifier que l'avis est trouvé avec le commentaire complet
        Optional<Review> foundReview = reviewRepository.findByReservationIdAndReviewerId(6L, user1.getId());
        
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getCommentaire()).isEqualTo(longComment);
        assertThat(foundReview.get().getCommentaire().length()).isEqualTo(1000);
    }
}
