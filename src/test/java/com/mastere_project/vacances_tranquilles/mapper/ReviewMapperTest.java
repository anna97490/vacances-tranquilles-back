package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewMapperTest {

    @Mock
    private UserRepository userRepository;

    private ReviewMapper reviewMapper;

    private User mockReviewer;
    private User mockReviewed;
    private Review mockReview;
    private ReviewDTO mockReviewDTO;

    @BeforeEach
    void setUp() {
        // Créer une instance réelle de ReviewMapperImpl avec le mock UserRepository
        reviewMapper = new com.mastere_project.vacances_tranquilles.mapper.impl.ReviewMapperImpl(userRepository);
        
        // Créer des utilisateurs de test
        mockReviewer = createMockUser(1L, "John", "Doe", UserRole.CLIENT);
        mockReviewed = createMockUser(2L, "Jane", "Smith", UserRole.PROVIDER);
        
        // Créer des objets de test
        mockReview = createMockReview();
        mockReviewDTO = createMockReviewDTO();
    }

    @Test
    void toDTO_ShouldReturnNull_WhenReviewIsNull() {
        ReviewDTO result = reviewMapper.toDTO(null);

        assertThat(result).isNull();
    }

    @Test
    void toDTO_ShouldMapReviewToDTO_WhenReviewIsValid() {
        ReviewDTO result = reviewMapper.toDTO(mockReview);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNote()).isEqualTo(5);
        assertThat(result.getCommentaire()).isEqualTo("Excellent service");
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getReviewerId()).isEqualTo(1L);
        assertThat(result.getReviewedId()).isEqualTo(2L);
        assertThat(result.getCreatedAt()).isEqualTo(mockReview.getCreatedAt());
    }

    @Test
    void toDTO_ShouldHandleNullReviewerAndReviewed() {
        Review reviewWithNullUsers = new Review();
        reviewWithNullUsers.setId(1L);
        reviewWithNullUsers.setNote(4);
        reviewWithNullUsers.setCommentaire("Good service");
        reviewWithNullUsers.setReservationId(1L);
        reviewWithNullUsers.setReviewer(null);
        reviewWithNullUsers.setReviewed(null);
        reviewWithNullUsers.setCreatedAt(LocalDateTime.now());

        ReviewDTO result = reviewMapper.toDTO(reviewWithNullUsers);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNote()).isEqualTo(4);
        assertThat(result.getCommentaire()).isEqualTo("Good service");
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getReviewerId()).isNull();
        assertThat(result.getReviewedId()).isNull();
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        Review result = reviewMapper.toEntity(null);

        assertThat(result).isNull();
    }

    @Test
    void toEntity_ShouldMapDTOToReview_WhenDTOIsValid() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockReviewer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockReviewed));

        Review result = reviewMapper.toEntity(mockReviewDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNote()).isEqualTo(5);
        assertThat(result.getCommentaire()).isEqualTo("Excellent service");
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getReviewer()).isEqualTo(mockReviewer);
        assertThat(result.getReviewed()).isEqualTo(mockReviewed);
        assertThat(result.getCreatedAt()).isEqualTo(mockReviewDTO.getCreatedAt());

        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
    }

    @Test
    void toEntity_ShouldHandleNullReviewerIdAndReviewedId() {
        ReviewDTO dtoWithNullIds = new ReviewDTO();
        dtoWithNullIds.setId(1L);
        dtoWithNullIds.setNote(4);
        dtoWithNullIds.setCommentaire("Good service");
        dtoWithNullIds.setReservationId(1L);
        dtoWithNullIds.setReviewerId(null);
        dtoWithNullIds.setReviewedId(null);
        dtoWithNullIds.setCreatedAt(LocalDateTime.now());

        Review result = reviewMapper.toEntity(dtoWithNullIds);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNote()).isEqualTo(4);
        assertThat(result.getCommentaire()).isEqualTo("Good service");
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getReviewer()).isNull();
        assertThat(result.getReviewed()).isNull();

        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void toEntity_ShouldHandleNonExistentUsers() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        when(userRepository.findById(888L)).thenReturn(Optional.empty());

        ReviewDTO dtoWithNonExistentUsers = new ReviewDTO();
        dtoWithNonExistentUsers.setId(1L);
        dtoWithNonExistentUsers.setNote(3);
        dtoWithNonExistentUsers.setCommentaire("Average service");
        dtoWithNonExistentUsers.setReservationId(1L);
        dtoWithNonExistentUsers.setReviewerId(999L);
        dtoWithNonExistentUsers.setReviewedId(888L);
        dtoWithNonExistentUsers.setCreatedAt(LocalDateTime.now());

        Review result = reviewMapper.toEntity(dtoWithNonExistentUsers);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNote()).isEqualTo(3);
        assertThat(result.getCommentaire()).isEqualTo("Average service");
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getReviewer()).isNull();
        assertThat(result.getReviewed()).isNull();

        verify(userRepository).findById(999L);
        verify(userRepository).findById(888L);
    }

    @Test
    void toEntity_ShouldHandlePartialUserResolution() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockReviewer));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ReviewDTO dtoWithPartialUsers = new ReviewDTO();
        dtoWithPartialUsers.setId(1L);
        dtoWithPartialUsers.setNote(4);
        dtoWithPartialUsers.setCommentaire("Good service");
        dtoWithPartialUsers.setReservationId(1L);
        dtoWithPartialUsers.setReviewerId(1L);
        dtoWithPartialUsers.setReviewedId(999L);
        dtoWithPartialUsers.setCreatedAt(LocalDateTime.now());

        Review result = reviewMapper.toEntity(dtoWithPartialUsers);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNote()).isEqualTo(4);
        assertThat(result.getCommentaire()).isEqualTo("Good service");
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getReviewer()).isEqualTo(mockReviewer);
        assertThat(result.getReviewed()).isNull();

        verify(userRepository).findById(1L);
        verify(userRepository).findById(999L);
    }

    @Test
    void roundTrip_ShouldPreserveData() {
        // Test aller-retour : Review -> DTO -> Review
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockReviewer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockReviewed));

        // Review -> DTO
        ReviewDTO dto = reviewMapper.toDTO(mockReview);
        assertThat(dto).isNotNull();

        // DTO -> Review
        Review result = reviewMapper.toEntity(dto);
        assertThat(result).isNotNull();

        // Vérifier que les données sont préservées
        assertThat(result.getId()).isEqualTo(mockReview.getId());
        assertThat(result.getNote()).isEqualTo(mockReview.getNote());
        assertThat(result.getCommentaire()).isEqualTo(mockReview.getCommentaire());
        assertThat(result.getReservationId()).isEqualTo(mockReview.getReservationId());
        assertThat(result.getReviewer()).isEqualTo(mockReview.getReviewer());
        assertThat(result.getReviewed()).isEqualTo(mockReview.getReviewed());
        assertThat(result.getCreatedAt()).isEqualTo(mockReview.getCreatedAt());
    }

    private Review createMockReview() {
        Review review = new Review();
        review.setId(1L);
        review.setNote(5);
        review.setCommentaire("Excellent service");
        review.setReservationId(1L);
        review.setReviewer(mockReviewer);
        review.setReviewed(mockReviewed);
        review.setCreatedAt(LocalDateTime.now());
        return review;
    }

    private ReviewDTO createMockReviewDTO() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(1L);
        dto.setNote(5);
        dto.setCommentaire("Excellent service");
        dto.setReservationId(1L);
        dto.setReviewerId(1L);
        dto.setReviewedId(2L);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    private User createMockUser(Long id, String firstName, String lastName, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(firstName.toLowerCase() + "@example.com");
        user.setUserRole(role);
        return user;
    }
} 