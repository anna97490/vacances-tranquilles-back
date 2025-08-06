package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewMapperImplTest {

    private UserRepository userRepository;
    private ReviewMapperImpl mapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        mapper = new ReviewMapperImpl(userRepository);
    }

    @Test
    @DisplayName("toDTO should map all fields correctly")
    void toDTO_shouldMapFields() {
        User reviewer = new User();
        reviewer.setId(1L);

        User reviewed = new User();
        reviewed.setId(2L);

        Review review = new Review();
        review.setId(100L);
        review.setNote(4);
        review.setCommentaire("Très bon service");
        review.setReservationId(55L);
        review.setReviewer(reviewer);
        review.setReviewed(reviewed);
        review.setCreatedAt(LocalDateTime.of(2024, 12, 1, 10, 30));

        ReviewDTO dto = mapper.toDTO(review);

        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getNote()).isEqualTo(4);
        assertThat(dto.getCommentaire()).isEqualTo("Très bon service");
        assertThat(dto.getReservationId()).isEqualTo(55L);
        assertThat(dto.getReviewerId()).isEqualTo(1L);
        assertThat(dto.getReviewedId()).isEqualTo(2L);
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 12, 1, 10, 30));
    }

    @Test
    @DisplayName("toEntity should map all fields and resolve reviewer/reviewed")
    void toEntity_shouldMapFieldsAndResolveUsers() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(101L);
        dto.setNote(5);
        dto.setCommentaire("Excellent !");
        dto.setReservationId(77L);
        dto.setReviewerId(10L);
        dto.setReviewedId(20L);
        dto.setCreatedAt(LocalDateTime.of(2025, 1, 15, 9, 45));

        User mockReviewer = new User();
        mockReviewer.setId(10L);
        User mockReviewed = new User();
        mockReviewed.setId(20L);

        when(userRepository.findById(10L)).thenReturn(Optional.of(mockReviewer));
        when(userRepository.findById(20L)).thenReturn(Optional.of(mockReviewed));

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(101L);
        assertThat(entity.getNote()).isEqualTo(5);
        assertThat(entity.getCommentaire()).isEqualTo("Excellent !");
        assertThat(entity.getReservationId()).isEqualTo(77L);
        assertThat(entity.getReviewer()).isEqualTo(mockReviewer);
        assertThat(entity.getReviewed()).isEqualTo(mockReviewed);
        assertThat(entity.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 1, 15, 9, 45));
    }

    @Test
    @DisplayName("toEntity should handle null reviewer/reviewed IDs")
    void toEntity_shouldHandleNullUserIds() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(102L);
        dto.setNote(3);
        dto.setCommentaire("Correct");
        dto.setReservationId(88L);
        dto.setReviewerId(null);
        dto.setReviewedId(null);
        dto.setCreatedAt(LocalDateTime.now());

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getReviewer()).isNull();
        assertThat(entity.getReviewed()).isNull();
    }
}