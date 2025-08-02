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

    // Tests pour les cas d'erreur dans ReviewMapper

    @Test
    @DisplayName("toDTO should return null when input is null")
    void toDTO_shouldReturnNullWhenInputIsNull() {
        ReviewDTO result = mapper.toDTO(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("toEntity should return null when input is null")
    void toEntity_shouldReturnNullWhenInputIsNull() {
        Review result = mapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("toDTO should handle null reviewer and reviewed")
    void toDTO_shouldHandleNullReviewerAndReviewed() {
        Review review = new Review();
        review.setId(103L);
        review.setNote(4);
        review.setCommentaire("Test comment");
        review.setReservationId(99L);
        review.setReviewer(null);
        review.setReviewed(null);
        review.setCreatedAt(LocalDateTime.now());

        ReviewDTO dto = mapper.toDTO(review);

        assertThat(dto.getId()).isEqualTo(103L);
        assertThat(dto.getNote()).isEqualTo(4);
        assertThat(dto.getCommentaire()).isEqualTo("Test comment");
        assertThat(dto.getReservationId()).isEqualTo(99L);
        assertThat(dto.getReviewerId()).isNull();
        assertThat(dto.getReviewedId()).isNull();
        assertThat(dto.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toDTO should handle null commentaire")
    void toDTO_shouldHandleNullCommentaire() {
        User reviewer = new User();
        reviewer.setId(1L);

        Review review = new Review();
        review.setId(104L);
        review.setNote(5);
        review.setCommentaire(null);
        review.setReservationId(100L);
        review.setReviewer(reviewer);
        review.setReviewed(null);
        review.setCreatedAt(LocalDateTime.now());

        ReviewDTO dto = mapper.toDTO(review);

        assertThat(dto.getId()).isEqualTo(104L);
        assertThat(dto.getNote()).isEqualTo(5);
        assertThat(dto.getCommentaire()).isNull();
        assertThat(dto.getReservationId()).isEqualTo(100L);
        assertThat(dto.getReviewerId()).isEqualTo(1L);
        assertThat(dto.getReviewedId()).isNull();
    }

    @Test
    @DisplayName("toDTO should handle null createdAt")
    void toDTO_shouldHandleNullCreatedAt() {
        Review review = new Review();
        review.setId(105L);
        review.setNote(3);
        review.setCommentaire("Test");
        review.setReservationId(101L);
        review.setReviewer(null);
        review.setReviewed(null);
        review.setCreatedAt(null);

        ReviewDTO dto = mapper.toDTO(review);

        assertThat(dto.getId()).isEqualTo(105L);
        assertThat(dto.getNote()).isEqualTo(3);
        assertThat(dto.getCommentaire()).isEqualTo("Test");
        assertThat(dto.getReservationId()).isEqualTo(101L);
        assertThat(dto.getReviewerId()).isNull();
        assertThat(dto.getReviewedId()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
    }

    // Tests pour les conversions avec des données null

    @Test
    @DisplayName("toEntity should handle null commentaire")
    void toEntity_shouldHandleNullCommentaire() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(106L);
        dto.setNote(4);
        dto.setCommentaire(null);
        dto.setReservationId(102L);
        dto.setReviewerId(5L);
        dto.setReviewedId(6L);
        dto.setCreatedAt(LocalDateTime.now());

        User mockReviewer = new User();
        mockReviewer.setId(5L);
        User mockReviewed = new User();
        mockReviewed.setId(6L);

        when(userRepository.findById(5L)).thenReturn(Optional.of(mockReviewer));
        when(userRepository.findById(6L)).thenReturn(Optional.of(mockReviewed));

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(106L);
        assertThat(entity.getNote()).isEqualTo(4);
        assertThat(entity.getCommentaire()).isNull();
        assertThat(entity.getReservationId()).isEqualTo(102L);
        assertThat(entity.getReviewer()).isEqualTo(mockReviewer);
        assertThat(entity.getReviewed()).isEqualTo(mockReviewed);
    }

    @Test
    @DisplayName("toEntity should handle null createdAt")
    void toEntity_shouldHandleNullCreatedAt() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(107L);
        dto.setNote(5);
        dto.setCommentaire("Test");
        dto.setReservationId(103L);
        dto.setReviewerId(null);
        dto.setReviewedId(null);
        dto.setCreatedAt(null);

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(107L);
        assertThat(entity.getNote()).isEqualTo(5);
        assertThat(entity.getCommentaire()).isEqualTo("Test");
        assertThat(entity.getReservationId()).isEqualTo(103L);
        assertThat(entity.getReviewer()).isNull();
        assertThat(entity.getReviewed()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
    }

    // Tests pour les cas limites dans les mappings

    @Test
    @DisplayName("toEntity should handle user not found in repository")
    void toEntity_shouldHandleUserNotFound() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(108L);
        dto.setNote(2);
        dto.setCommentaire("Test");
        dto.setReservationId(104L);
        dto.setReviewerId(999L); // User ID that doesn't exist
        dto.setReviewedId(888L); // User ID that doesn't exist
        dto.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        when(userRepository.findById(888L)).thenReturn(Optional.empty());

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(108L);
        assertThat(entity.getNote()).isEqualTo(2);
        assertThat(entity.getCommentaire()).isEqualTo("Test");
        assertThat(entity.getReservationId()).isEqualTo(104L);
        assertThat(entity.getReviewer()).isNull();
        assertThat(entity.getReviewed()).isNull();
        assertThat(entity.getCreatedAt()).isNotNull();

        verify(userRepository).findById(999L);
        verify(userRepository).findById(888L);
    }

    @Test
    @DisplayName("toEntity should handle partial user resolution")
    void toEntity_shouldHandlePartialUserResolution() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(109L);
        dto.setNote(4);
        dto.setCommentaire("Test");
        dto.setReservationId(105L);
        dto.setReviewerId(7L);
        dto.setReviewedId(999L); // User ID that doesn't exist
        dto.setCreatedAt(LocalDateTime.now());

        User mockReviewer = new User();
        mockReviewer.setId(7L);

        when(userRepository.findById(7L)).thenReturn(Optional.of(mockReviewer));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(109L);
        assertThat(entity.getNote()).isEqualTo(4);
        assertThat(entity.getCommentaire()).isEqualTo("Test");
        assertThat(entity.getReservationId()).isEqualTo(105L);
        assertThat(entity.getReviewer()).isEqualTo(mockReviewer);
        assertThat(entity.getReviewed()).isNull();
        assertThat(entity.getCreatedAt()).isNotNull();

        verify(userRepository).findById(7L);
        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("toDTO should handle edge case values")
    void toDTO_shouldHandleEdgeCaseValues() {
        Review review = new Review();
        review.setId(0L); // Edge case: ID = 0
        review.setNote(1); // Edge case: minimum note
        review.setCommentaire(""); // Edge case: empty string
        review.setReservationId(0L); // Edge case: ID = 0
        review.setReviewer(null);
        review.setReviewed(null);
        review.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0)); // Edge case: old date

        ReviewDTO dto = mapper.toDTO(review);

        assertThat(dto.getId()).isEqualTo(0L);
        assertThat(dto.getNote()).isEqualTo(1);
        assertThat(dto.getCommentaire()).isEqualTo("");
        assertThat(dto.getReservationId()).isEqualTo(0L);
        assertThat(dto.getReviewerId()).isNull();
        assertThat(dto.getReviewedId()).isNull();
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.of(2020, 1, 1, 0, 0));
    }

    @Test
    @DisplayName("toEntity should handle edge case values")
    void toEntity_shouldHandleEdgeCaseValues() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(0L); // Edge case: ID = 0
        dto.setNote(5); // Edge case: maximum note
        dto.setCommentaire(""); // Edge case: empty string
        dto.setReservationId(0L); // Edge case: ID = 0
        dto.setReviewerId(null);
        dto.setReviewedId(null);
        dto.setCreatedAt(LocalDateTime.of(2030, 12, 31, 23, 59)); // Edge case: future date

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(0L);
        assertThat(entity.getNote()).isEqualTo(5);
        assertThat(entity.getCommentaire()).isEqualTo("");
        assertThat(entity.getReservationId()).isEqualTo(0L);
        assertThat(entity.getReviewer()).isNull();
        assertThat(entity.getReviewed()).isNull();
        assertThat(entity.getCreatedAt()).isEqualTo(LocalDateTime.of(2030, 12, 31, 23, 59));
    }

    @Test
    @DisplayName("toDTO should handle very long commentaire")
    void toDTO_shouldHandleVeryLongCommentaire() {
        String longComment = "A".repeat(1000); // Very long comment
        
        Review review = new Review();
        review.setId(110L);
        review.setNote(3);
        review.setCommentaire(longComment);
        review.setReservationId(106L);
        review.setReviewer(null);
        review.setReviewed(null);
        review.setCreatedAt(LocalDateTime.now());

        ReviewDTO dto = mapper.toDTO(review);

        assertThat(dto.getId()).isEqualTo(110L);
        assertThat(dto.getNote()).isEqualTo(3);
        assertThat(dto.getCommentaire()).isEqualTo(longComment);
        assertThat(dto.getCommentaire().length()).isEqualTo(1000);
        assertThat(dto.getReservationId()).isEqualTo(106L);
        assertThat(dto.getReviewerId()).isNull();
        assertThat(dto.getReviewedId()).isNull();
    }

    @Test
    @DisplayName("toEntity should handle very long commentaire")
    void toEntity_shouldHandleVeryLongCommentaire() {
        String longComment = "B".repeat(1000); // Very long comment
        
        ReviewDTO dto = new ReviewDTO();
        dto.setId(111L);
        dto.setNote(4);
        dto.setCommentaire(longComment);
        dto.setReservationId(107L);
        dto.setReviewerId(null);
        dto.setReviewedId(null);
        dto.setCreatedAt(LocalDateTime.now());

        Review entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(111L);
        assertThat(entity.getNote()).isEqualTo(4);
        assertThat(entity.getCommentaire()).isEqualTo(longComment);
        assertThat(entity.getCommentaire().length()).isEqualTo(1000);
        assertThat(entity.getReservationId()).isEqualTo(107L);
        assertThat(entity.getReviewer()).isNull();
        assertThat(entity.getReviewed()).isNull();
    }
}
