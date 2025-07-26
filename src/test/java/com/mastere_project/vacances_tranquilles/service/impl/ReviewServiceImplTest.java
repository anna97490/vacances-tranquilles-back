package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.exception.*;
import com.mastere_project.vacances_tranquilles.mapper.ReviewMapper;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    private ReviewRepository reviewRepository;
    private ReservationRepository reservationRepository;
    private ReviewMapper reviewMapper;
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        reviewMapper = mock(ReviewMapper.class);
        reviewService = new ReviewServiceImpl(reviewRepository, reservationRepository, reviewMapper);
    }

    @Test
    @DisplayName("createReview should throw if note is invalid")
    void createReview_shouldThrow_whenNoteInvalid() {
        ReviewDTO dto = new ReviewDTO();
        dto.setNote(6);

        assertThatThrownBy(() -> reviewService.createReview(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La note doit être comprise entre 1 et 5");
    }

    @Test
    @DisplayName("createReview should throw if reservation not found")
    void createReview_shouldThrow_whenReservationNotFound() {
        ReviewDTO dto = new ReviewDTO();
        dto.setNote(4);
        dto.setReservationId(123L);

        when(reservationRepository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.createReview(dto))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("Réservation introuvable");
    }

    @Test
    @DisplayName("createReview should throw if reservation is not closed")
    void createReview_shouldThrow_whenReservationNotClosed() {
        ReviewDTO dto = new ReviewDTO();
        dto.setNote(4);
        dto.setReservationId(123L);

        Reservation reservation = new Reservation();
        reservation.setStatus("pending");

        when(reservationRepository.findById(123L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reviewService.createReview(dto))
                .isInstanceOf(ReservationNotCompletedException.class);
    }

    @Test
    @DisplayName("createReview should throw if users do not match reservation")
    void createReview_shouldThrow_whenUsersInvalid() {
        ReviewDTO dto = new ReviewDTO();
        dto.setNote(4);
        dto.setReservationId(123L);
        dto.setReviewerId(1L);
        dto.setReviewedId(2L);

        Reservation reservation = new Reservation();
        reservation.setStatus("closed");
        reservation.setClientId(999L);
        reservation.setProviderId(888L);

        when(reservationRepository.findById(123L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reviewService.createReview(dto))
                .isInstanceOf(InvalidReviewUserException.class);
    }

    @Test
    @DisplayName("createReview should save and return DTO when valid")
    void createReview_shouldSave_whenValid() {
        ReviewDTO dto = new ReviewDTO();
        dto.setNote(5);
        dto.setReservationId(1L);
        dto.setReviewerId(10L);
        dto.setReviewedId(20L);

        Reservation reservation = new Reservation();
        reservation.setStatus("closed");
        reservation.setClientId(10L);
        reservation.setProviderId(20L);

        Review reviewEntity = new Review();
        Review savedEntity = new Review();
        ReviewDTO returnedDto = new ReviewDTO();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reviewMapper.toEntity(dto)).thenReturn(reviewEntity);
        when(reviewRepository.save(any(Review.class))).thenReturn(savedEntity);
        when(reviewMapper.toDTO(savedEntity)).thenReturn(returnedDto);

        ReviewDTO result = reviewService.createReview(dto);

        assertThat(result).isEqualTo(returnedDto);
    }

    @Test
    @DisplayName("getReviewById should throw if not found")
    void getReviewById_shouldThrow_whenNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getReviewById(99L))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    @DisplayName("getReviewById should return DTO when found")
    void getReviewById_shouldReturnDTO_whenFound() {
        Review review = new Review();
        ReviewDTO dto = new ReviewDTO();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewMapper.toDTO(review)).thenReturn(dto);

        ReviewDTO result = reviewService.getReviewById(1L);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    @DisplayName("getReviewsForUser should return mapped DTOs")
    void getReviewsForUser_shouldReturnDTOs() {
        Review r1 = new Review();
        Review r2 = new Review();
        ReviewDTO dto1 = new ReviewDTO();
        ReviewDTO dto2 = new ReviewDTO();

        when(reviewRepository.findByReviewedId(5L)).thenReturn(List.of(r1, r2));
        when(reviewMapper.toDTO(r1)).thenReturn(dto1);
        when(reviewMapper.toDTO(r2)).thenReturn(dto2);

        List<ReviewDTO> result = reviewService.getReviewsForUser(5L);

        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    @DisplayName("getReviewsWrittenByUser should return mapped DTOs")
    void getReviewsWrittenByUser_shouldReturnDTOs() {
        Review r1 = new Review();
        ReviewDTO dto1 = new ReviewDTO();

        when(reviewRepository.findByReviewerId(3L)).thenReturn(List.of(r1));
        when(reviewMapper.toDTO(r1)).thenReturn(dto1);

        List<ReviewDTO> result = reviewService.getReviewsWrittenByUser(3L);

        assertThat(result).containsExactly(dto1);
    }

    @Test
    @DisplayName("getReviewsFromUserToUser should return mapped DTOs")
    void getReviewsFromUserToUser_shouldReturnDTOs() {
        Review r = new Review();
        ReviewDTO dto = new ReviewDTO();

        when(reviewRepository.findByReviewerIdAndReviewedId(1L, 2L)).thenReturn(List.of(r));
        when(reviewMapper.toDTO(r)).thenReturn(dto);

        List<ReviewDTO> result = reviewService.getReviewsFromUserToUser(1L, 2L);

        assertThat(result).containsExactly(dto);
    }
}
