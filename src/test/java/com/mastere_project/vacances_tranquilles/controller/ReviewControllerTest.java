package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.dto.ReviewWithReviewerDTO;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotCompletedException;
import com.mastere_project.vacances_tranquilles.exception.InvalidReviewUserException;
import com.mastere_project.vacances_tranquilles.exception.ReviewNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReviewAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDTO mockReviewDTO;
    private ReviewWithReviewerDTO mockReviewWithReviewerDTO;

    @BeforeEach
    void setUp() {
        mockReviewDTO = new ReviewDTO();
        mockReviewDTO.setId(1L);
        mockReviewDTO.setNote(5);
        mockReviewDTO.setCommentaire("Excellent service");
        mockReviewDTO.setReviewerId(1L);
        mockReviewDTO.setReviewedId(2L);
        mockReviewDTO.setReservationId(1L);

        mockReviewWithReviewerDTO = new ReviewWithReviewerDTO();
        mockReviewWithReviewerDTO.setId(1L);
        mockReviewWithReviewerDTO.setNote(5);
        mockReviewWithReviewerDTO.setCommentaire("Excellent service");
        mockReviewWithReviewerDTO.setReviewerId(1L);
        mockReviewWithReviewerDTO.setReviewedId(2L);
        mockReviewWithReviewerDTO.setReservationId(1L);
        mockReviewWithReviewerDTO.setReviewerFirstName("Jean");
    }

    @Test
    void createReview_ShouldReturnCreatedReview() {
        // Le service va automatiquement définir reviewerId et reviewedId
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setNote(5);
        inputDTO.setCommentaire("Excellent service");
        inputDTO.setReservationId(1L);
        inputDTO.setReviewerId(0L); 
        inputDTO.setReviewedId(0L);

        when(reviewService.createReview(any(ReviewDTO.class))).thenReturn(mockReviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(inputDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getNote()).isEqualTo(5);
        assertThat(response.getBody().getCommentaire()).isEqualTo("Excellent service");
        assertThat(response.getBody().getReviewerId()).isEqualTo(1L);
        assertThat(response.getBody().getReviewedId()).isEqualTo(2L);

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    void getReviewById_ShouldReturnReview() {
        when(reviewService.getReviewById(1L)).thenReturn(mockReviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.getReviewById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getNote()).isEqualTo(5);
        assertThat(response.getBody().getCommentaire()).isEqualTo("Excellent service");

        verify(reviewService, times(1)).getReviewById(1L);
    }

    @Test
    void getReviewsWrittenByUser_ShouldReturnReviewsList() {
        List<ReviewDTO> reviews = Arrays.asList(mockReviewDTO);
        when(reviewService.getReviewsWrittenByUser()).thenReturn(reviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsWrittenByUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().get(0).getNote()).isEqualTo(5);
        assertThat(response.getBody().get(0).getCommentaire()).isEqualTo("Excellent service");

        verify(reviewService, times(1)).getReviewsWrittenByUser();
    }

    @Test
    void getReviewsReceivedByUser_ShouldReturnReviewsList() {
        List<ReviewDTO> reviews = Arrays.asList(mockReviewDTO);
        when(reviewService.getReviewsReceivedByUser()).thenReturn(reviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsReceivedByUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().get(0).getNote()).isEqualTo(5);
        assertThat(response.getBody().get(0).getCommentaire()).isEqualTo("Excellent service");

        verify(reviewService, times(1)).getReviewsReceivedByUser();
    }

    @Test
    void getReviewsByProviderId_ShouldReturnReviewsList() {
        List<ReviewDTO> reviews = Arrays.asList(mockReviewDTO);
        when(reviewService.getReviewsByProviderId(1L)).thenReturn(reviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByProviderId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().get(0).getNote()).isEqualTo(5);
        assertThat(response.getBody().get(0).getCommentaire()).isEqualTo("Excellent service");

        verify(reviewService, times(1)).getReviewsByProviderId(1L);
    }

    @Test
    void getReviewsWithReviewerByProviderId_ShouldReturnReviewsWithReviewerList() {
        List<ReviewWithReviewerDTO> reviews = Arrays.asList(mockReviewWithReviewerDTO);
        when(reviewService.getReviewsWithReviewerByProviderId(1L)).thenReturn(reviews);

        ResponseEntity<List<ReviewWithReviewerDTO>> response = reviewController.getReviewsWithReviewerByProviderId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().get(0).getNote()).isEqualTo(5);
        assertThat(response.getBody().get(0).getCommentaire()).isEqualTo("Excellent service");
        assertThat(response.getBody().get(0).getReviewerFirstName()).isEqualTo("Jean");

        verify(reviewService, times(1)).getReviewsWithReviewerByProviderId(1L);
    }

    @Test
    void getReviewsByReservationId_ShouldReturnReviewsList() {
        List<ReviewDTO> reviews = Arrays.asList(mockReviewDTO);
        when(reviewService.getReviewsByReservationId(1L)).thenReturn(reviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByReservationId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().get(0).getNote()).isEqualTo(5);
        assertThat(response.getBody().get(0).getCommentaire()).isEqualTo("Excellent service");
        assertThat(response.getBody().get(0).getReservationId()).isEqualTo(1L);

        verify(reviewService, times(1)).getReviewsByReservationId(1L);
    }

    @Test
    void getReviewsWrittenByUser_WhenNoReviews_ShouldReturnEmptyList() {
        when(reviewService.getReviewsWrittenByUser()).thenReturn(Arrays.asList());

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsWrittenByUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(reviewService, times(1)).getReviewsWrittenByUser();
    }

    @Test
    void getReviewsReceivedByUser_WhenNoReviews_ShouldReturnEmptyList() {
        when(reviewService.getReviewsReceivedByUser()).thenReturn(Arrays.asList());

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsReceivedByUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(reviewService, times(1)).getReviewsReceivedByUser();
    }

    @Test
    void getReviewsByProviderId_WhenNoReviews_ShouldReturnEmptyList() {
        when(reviewService.getReviewsByProviderId(1L)).thenReturn(Arrays.asList());

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByProviderId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(reviewService, times(1)).getReviewsByProviderId(1L);
    }

    @Test
    void getReviewsWithReviewerByProviderId_WhenNoReviews_ShouldReturnEmptyList() {
        when(reviewService.getReviewsWithReviewerByProviderId(1L)).thenReturn(Arrays.asList());

        ResponseEntity<List<ReviewWithReviewerDTO>> response = reviewController.getReviewsWithReviewerByProviderId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(reviewService, times(1)).getReviewsWithReviewerByProviderId(1L);
    }

    @Test
    void getReviewsByReservationId_WhenNoReviews_ShouldReturnEmptyList() {
        when(reviewService.getReviewsByReservationId(1L)).thenReturn(Arrays.asList());

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByReservationId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(reviewService, times(1)).getReviewsByReservationId(1L);
    }

    @Test
    void createReview_WhenReservationNotFound_ShouldThrowException() {
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setNote(5);
        inputDTO.setCommentaire("Excellent service");
        inputDTO.setReservationId(999L);
        inputDTO.setReviewerId(0L);
        inputDTO.setReviewedId(0L);

        when(reviewService.createReview(any(ReviewDTO.class)))
                .thenThrow(new ReservationNotFoundException("Réservation introuvable"));

        assertThatThrownBy(() -> reviewController.createReview(inputDTO))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessage("Réservation introuvable");

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    void createReview_WhenReservationNotClosed_ShouldThrowException() {
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setNote(5);
        inputDTO.setCommentaire("Excellent service");
        inputDTO.setReservationId(1L);
        inputDTO.setReviewerId(0L);
        inputDTO.setReviewedId(0L);

        when(reviewService.createReview(any(ReviewDTO.class)))
                .thenThrow(new ReservationNotCompletedException("La réservation doit avoir le statut CLOSED pour pouvoir écrire un avis"));

        assertThatThrownBy(() -> reviewController.createReview(inputDTO))
                .isInstanceOf(ReservationNotCompletedException.class)
                .hasMessage("La réservation doit avoir le statut CLOSED pour pouvoir écrire un avis");

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    void createReview_WhenInvalidReviewUser_ShouldThrowException() {
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setNote(5);
        inputDTO.setCommentaire("Excellent service");
        inputDTO.setReservationId(1L);
        inputDTO.setReviewerId(0L);
        inputDTO.setReviewedId(0L);

        when(reviewService.createReview(any(ReviewDTO.class)))
                .thenThrow(new InvalidReviewUserException("Vous ne pouvez évaluer que les utilisateurs impliqués dans cette réservation"));

        assertThatThrownBy(() -> reviewController.createReview(inputDTO))
                .isInstanceOf(InvalidReviewUserException.class)
                .hasMessage("Vous ne pouvez évaluer que les utilisateurs impliqués dans cette réservation");

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    void getReviewById_WhenReviewNotFound_ShouldThrowException() {
        when(reviewService.getReviewById(999L))
                .thenThrow(new ReviewNotFoundException("Avis introuvable"));

        assertThatThrownBy(() -> reviewController.getReviewById(999L))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessage("Avis introuvable");

        verify(reviewService, times(1)).getReviewById(999L);
    }

    @Test
    void createReview_WhenReviewAlreadyExists_ShouldThrowException() {
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setNote(5);
        inputDTO.setCommentaire("Excellent service");
        inputDTO.setReservationId(1L);
        inputDTO.setReviewerId(0L);
        inputDTO.setReviewedId(0L);

        when(reviewService.createReview(any(ReviewDTO.class)))
                .thenThrow(new ReviewAlreadyExistsException("Vous avez déjà créé un avis pour cette réservation. Un seul avis par réservation est autorisé."));

        assertThatThrownBy(() -> reviewController.createReview(inputDTO))
                .isInstanceOf(ReviewAlreadyExistsException.class)
                .hasMessage("Vous avez déjà créé un avis pour cette réservation. Un seul avis par réservation est autorisé.");

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    void createReview_WithMinimalData_ShouldReturnCreatedReview() {
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setNote(3);
        inputDTO.setReservationId(1L);
        inputDTO.setReviewerId(0L);
        inputDTO.setReviewedId(0L);
        // Pas de commentaire

        ReviewDTO expectedResponse = new ReviewDTO();
        expectedResponse.setId(2L);
        expectedResponse.setNote(3);
        expectedResponse.setCommentaire(null);
        expectedResponse.setReviewerId(1L);
        expectedResponse.setReviewedId(2L);
        expectedResponse.setReservationId(1L);

        when(reviewService.createReview(any(ReviewDTO.class))).thenReturn(expectedResponse);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(inputDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(2L);
        assertThat(response.getBody().getNote()).isEqualTo(3);
        assertThat(response.getBody().getCommentaire()).isNull();

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    void getReviewsWrittenByUser_WithMultipleReviews_ShouldReturnAllReviews() {
        ReviewDTO review1 = new ReviewDTO();
        review1.setId(1L);
        review1.setNote(5);
        review1.setCommentaire("Excellent");

        ReviewDTO review2 = new ReviewDTO();
        review2.setId(2L);
        review2.setNote(4);
        review2.setCommentaire("Très bien");

        List<ReviewDTO> reviews = Arrays.asList(review1, review2);
        when(reviewService.getReviewsWrittenByUser()).thenReturn(reviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsWrittenByUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().get(1).getId()).isEqualTo(2L);

        verify(reviewService, times(1)).getReviewsWrittenByUser();
    }

    @Test
    void getReviewsWithReviewerByProviderId_WithMultipleReviews_ShouldReturnAllReviewsWithReviewerInfo() {
        ReviewWithReviewerDTO review1 = new ReviewWithReviewerDTO();
        review1.setId(1L);
        review1.setNote(5);
        review1.setReviewerFirstName("Jean");

        ReviewWithReviewerDTO review2 = new ReviewWithReviewerDTO();
        review2.setId(2L);
        review2.setNote(4);
        review2.setReviewerFirstName("Marie");

        List<ReviewWithReviewerDTO> reviews = Arrays.asList(review1, review2);
        when(reviewService.getReviewsWithReviewerByProviderId(1L)).thenReturn(reviews);

        ResponseEntity<List<ReviewWithReviewerDTO>> response = reviewController.getReviewsWithReviewerByProviderId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getReviewerFirstName()).isEqualTo("Jean");
        assertThat(response.getBody().get(1).getReviewerFirstName()).isEqualTo("Marie");

        verify(reviewService, times(1)).getReviewsWithReviewerByProviderId(1L);
    }
} 