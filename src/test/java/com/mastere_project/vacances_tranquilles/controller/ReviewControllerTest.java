package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDTO mockReviewDTO;

    @BeforeEach
    void setUp() {
        mockReviewDTO = new ReviewDTO();
        mockReviewDTO.setId(1L);
        mockReviewDTO.setNote(5);
        mockReviewDTO.setCommentaire("Excellent service");
        mockReviewDTO.setReviewerId(1L);
        mockReviewDTO.setReviewedId(2L);
        mockReviewDTO.setReservationId(1L);
    }

    @Test
    void createReview_ShouldReturnCreatedReview() {
        when(reviewService.createReview(any(ReviewDTO.class))).thenReturn(mockReviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(mockReviewDTO);

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
} 