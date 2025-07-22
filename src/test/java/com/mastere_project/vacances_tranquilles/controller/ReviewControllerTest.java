package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createReview - should return created review")
    void createReview_shouldReturnCreatedReview() {
        ReviewDTO input = new ReviewDTO();
        input.setNote(5);
        input.setCommentaire("Test");
        input.setReservationId(1L);
        input.setReviewerId(2L);
        input.setReviewedId(3L);
        ReviewDTO output = new ReviewDTO();
        output.setId(10L);
        when(reviewService.createReview(input)).thenReturn(output);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(input);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(output);
    }

    @Test
    @DisplayName("getReviewById - should return review")
    void getReviewById_shouldReturnReview() {
        ReviewDTO review = new ReviewDTO();
        review.setId(1L);
        when(reviewService.getReviewById(1L)).thenReturn(review);

        ResponseEntity<ReviewDTO> response = reviewController.getReviewById(1L);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(review);
    }

    @Test
    @DisplayName("getReviewsFromUserToUser - should return list of reviews from one user to another")
    void getReviewsFromUserToUser_shouldReturnList() {
        ReviewDTO review = new ReviewDTO();
        review.setId(1L);
        List<ReviewDTO> reviews = Collections.singletonList(review);
        when(reviewService.getReviewsFromUserToUser(2L, 3L)).thenReturn(reviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsFromUserToUser(2L, 3L);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(review);
    }

    @Test
    @DisplayName("getReviewsWrittenByUser - should return list of reviews written")
    void getReviewsWrittenByUser_shouldReturnList() {
        ReviewDTO review1 = new ReviewDTO();
        review1.setId(1L);
        ReviewDTO review2 = new ReviewDTO();
        review2.setId(2L);
        List<ReviewDTO> reviews = Arrays.asList(review1, review2);
        when(reviewService.getReviewsWrittenByUser(2L)).thenReturn(reviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsWrittenByUser(2L);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(review1, review2);
    }
} 