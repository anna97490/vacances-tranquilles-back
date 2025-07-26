package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ReviewController reviewController;



    @Test
    @DisplayName("createReview - should return created review with authenticated user")
    @WithMockUser
    void createReview_shouldReturnCreatedReview() {
        when(principal.getName()).thenReturn("123");
        
        ReviewDTO input = new ReviewDTO();
        input.setNote(5);
        input.setCommentaire("Test");
        input.setReservationId(1L);
        input.setReviewedId(3L);
        
        ReviewDTO output = new ReviewDTO();
        output.setId(10L);
        output.setReviewerId(123L);
        
        when(reviewService.createReview(any(ReviewDTO.class))).thenReturn(output);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(input, principal);
        
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(output);
        verify(reviewService).createReview(argThat(dto -> dto.getReviewerId().equals(123L)));
    }

    @Test
    @DisplayName("getReviewById - should return review")
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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