package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.dto.ReviewWithReviewerDTO;
import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.InvalidReviewUserException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotCompletedException;
import com.mastere_project.vacances_tranquilles.exception.ReviewNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReviewAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.mapper.ReviewMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ReviewRepository;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review mockReview;
    private ReviewDTO mockReviewDTO;
    private User mockUser;
    private Reservation mockReservation;

    @BeforeEach
    void setUp() {
        mockUser = createMockUser();
        mockReview = createMockReview();
        mockReviewDTO = createMockReviewDTO();
        mockReservation = createMockReservation();
    }

    @Test
    void createReview_ShouldCreateReviewSuccessfully() {
        Long currentUserId = 1L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(mockReviewDTO.getReservationId())).thenReturn(Optional.of(mockReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId)).thenReturn(false);
            when(reviewMapper.toEntity(any(ReviewDTO.class))).thenReturn(mockReview);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);
            
            ReviewDTO result = reviewService.createReview(mockReviewDTO);
            
            assertNotNull(result);
            assertEquals(mockReviewDTO, result);
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
            verify(reviewRepository).existsByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId);
            verify(reviewMapper).toEntity(any(ReviewDTO.class));
            verify(reviewRepository).save(any(Review.class));
            verify(reviewMapper).toDTO(mockReview);
        }
    }

    @Test
    void createReview_WhenUserNotFound_ShouldThrowException() {
        Long currentUserId = 999L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            assertEquals("Utilisateur non trouvé", exception.getMessage());
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository, never()).findById(any());
            verify(reviewRepository, never()).existsByReservationIdAndReviewerId(any(), any());
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenUserRoleIsNull_ShouldThrowException() {
        Long currentUserId = 1L;
        mockUser.setUserRole(null);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            assertEquals("Rôle utilisateur non défini en base de données", exception.getMessage());
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository, never()).findById(any());
            verify(reviewRepository, never()).existsByReservationIdAndReviewerId(any(), any());
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenReservationNotFound_ShouldThrowException() {
        Long currentUserId = 1L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(mockReviewDTO.getReservationId())).thenReturn(Optional.empty());
            
            ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            assertEquals("Réservation introuvable", exception.getMessage());
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
            verify(reviewRepository, never()).existsByReservationIdAndReviewerId(any(), any());
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenReservationStatusNotClosed_ShouldThrowException() {
        Long currentUserId = 1L;
        mockReservation.setStatus(com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus.IN_PROGRESS);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(mockReviewDTO.getReservationId())).thenReturn(Optional.of(mockReservation));
            
            ReservationNotCompletedException exception = assertThrows(ReservationNotCompletedException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            assertEquals("La réservation doit avoir le statut CLOSED pour pouvoir écrire un avis", exception.getMessage());
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
            verify(reviewRepository, never()).existsByReservationIdAndReviewerId(any(), any());
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenReviewAlreadyExists_ShouldThrowException() {
        Long currentUserId = 1L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(mockReviewDTO.getReservationId())).thenReturn(Optional.of(mockReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId)).thenReturn(true);
            
            ReviewAlreadyExistsException exception = assertThrows(ReviewAlreadyExistsException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            
            assertEquals("Vous avez déjà créé un avis pour cette réservation. Un seul avis par réservation est autorisé.", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
            verify(reviewRepository).existsByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId);
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenNoteIsInvalid_ShouldThrowException() {
        Long currentUserId = 1L;
        ReviewDTO invalidReviewDTO = createMockReviewDTO();
        invalidReviewDTO.setNote(0); // Note invalide
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> reviewService.createReview(invalidReviewDTO));
            
            assertEquals("La note doit être comprise entre 1 et 5.", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository, never()).findById(any());
            verify(reviewRepository, never()).existsByReservationIdAndReviewerId(any(), any());
        }
    }

    @Test
    void createReview_WhenNoteIsTooHigh_ShouldThrowException() {
        Long currentUserId = 1L;
        ReviewDTO invalidReviewDTO = createMockReviewDTO();
        invalidReviewDTO.setNote(6); // Note trop élevée
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> reviewService.createReview(invalidReviewDTO));
            
            assertEquals("La note doit être comprise entre 1 et 5.", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository, never()).findById(any());
            verify(reviewRepository, never()).existsByReservationIdAndReviewerId(any(), any());
        }
    }

    @Test
    void createReview_WhenUsersDoNotMatchReservation_ShouldThrowException() {
        Long currentUserId = 3L; 
        ReviewDTO invalidReviewDTO = createMockReviewDTO();
        invalidReviewDTO.setReviewerId(3L);
        invalidReviewDTO.setReviewedId(4L);
        
        Reservation invalidReservation = createMockReservation();
        User client = createMockUser();
        client.setId(1L);
        User provider = createMockUser();
        provider.setId(2L);
        invalidReservation.setClient(client);
        invalidReservation.setProvider(provider);
        invalidReservation.setStatus(com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus.CLOSED);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(invalidReviewDTO.getReservationId())).thenReturn(Optional.of(invalidReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(invalidReviewDTO.getReservationId(), currentUserId)).thenReturn(false);
            
            InvalidReviewUserException exception = assertThrows(InvalidReviewUserException.class, 
                () -> reviewService.createReview(invalidReviewDTO));
            
            assertEquals("Vous ne pouvez évaluer que les utilisateurs impliqués dans cette réservation", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(invalidReviewDTO.getReservationId());
            verify(reviewRepository).existsByReservationIdAndReviewerId(invalidReviewDTO.getReservationId(), currentUserId);
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void getReviewById_ShouldReturnReview() {
        Long reviewId = 1L;
        Long currentUserId = 1L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));
            when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);
            
            ReviewDTO result = reviewService.getReviewById(reviewId);
            
            assertNotNull(result);
            assertEquals(mockReviewDTO, result);
            
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository).findById(reviewId);
            verify(reviewMapper).toDTO(mockReview);
        }
    }

    @Test
    void getReviewById_WhenReviewNotFound_ShouldThrowException() {
        Long reviewId = 999L;
        Long currentUserId = 1L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());
            
            ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class, 
                () -> reviewService.getReviewById(reviewId));
            assertEquals("Avis introuvable", exception.getMessage());
            
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository).findById(reviewId);
            verify(reviewMapper, never()).toDTO(any());
        }
    }

    @Test
    void getReviewById_WhenUserNotFound_ShouldThrowException() {
        Long currentUserId = 999L;
        Long reviewId = 1L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.getReviewById(reviewId));
            
            assertEquals("Utilisateur non trouvé", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository, never()).findById(any());
        }
    }

    @Test
    void getReviewById_WhenUserRoleIsNull_ShouldThrowException() {
        Long currentUserId = 1L;
        Long reviewId = 1L;
        User userWithNullRole = createMockUser();
        userWithNullRole.setUserRole(null);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userWithNullRole));
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.getReviewById(reviewId));
            
            assertEquals("Rôle utilisateur non défini en base de données", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository, never()).findById(any());
        }
    }

    @Test
    void getReviewsWrittenByUser_ShouldReturnReviewsList() {
        Long currentUserId = 1L;
        List<Review> reviews = Arrays.asList(mockReview);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findByReviewerId(currentUserId)).thenReturn(reviews);
            when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);
            
            List<ReviewDTO> result = reviewService.getReviewsWrittenByUser();
            
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(mockReviewDTO, result.get(0));
            
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository).findByReviewerId(currentUserId);
            verify(reviewMapper).toDTO(mockReview);
        }
    }

    @Test
    void getReviewsWrittenByUser_WhenUserNotFound_ShouldThrowException() {
        Long currentUserId = 999L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.getReviewsWrittenByUser());
            
            assertEquals("Utilisateur non trouvé", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository, never()).findByReviewerId(any());
        }
    }

    @Test
    void getReviewsWrittenByUser_WhenUserRoleIsNull_ShouldThrowException() {
        Long currentUserId = 1L;
        User userWithNullRole = createMockUser();
        userWithNullRole.setUserRole(null);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userWithNullRole));
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.getReviewsWrittenByUser());
            
            assertEquals("Rôle utilisateur non défini en base de données", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository, never()).findByReviewerId(any());
        }
    }

    @Test
    void getReviewsReceivedByUser_ShouldReturnReviewsList() {
        Long currentUserId = 1L;
        List<Review> reviews = Arrays.asList(mockReview);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findByReviewedId(currentUserId)).thenReturn(reviews);
            when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);
            
            List<ReviewDTO> result = reviewService.getReviewsReceivedByUser();
            
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(mockReviewDTO, result.get(0));
            
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository).findByReviewedId(currentUserId);
            verify(reviewMapper).toDTO(mockReview);
        }
    }

    @Test
    void getReviewsReceivedByUser_WhenUserNotFound_ShouldThrowException() {
        Long currentUserId = 999L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.getReviewsReceivedByUser());
            
            assertEquals("Utilisateur non trouvé", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository, never()).findByReviewedId(any());
        }
    }

    @Test
    void getReviewsReceivedByUser_WhenUserRoleIsNull_ShouldThrowException() {
        Long currentUserId = 1L;
        User userWithNullRole = createMockUser();
        userWithNullRole.setUserRole(null);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userWithNullRole));
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
                () -> reviewService.getReviewsReceivedByUser());
            
            assertEquals("Rôle utilisateur non défini en base de données", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository, never()).findByReviewedId(any());
        }
    }

    @Test
    void getReviewsByProviderId_ShouldReturnReviewsList() {
        Long providerId = 2L;
        List<Review> mockReviews = Arrays.asList(mockReview);
        
        when(reviewRepository.findByReviewedId(providerId)).thenReturn(mockReviews);
        when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);

        List<ReviewDTO> result = reviewService.getReviewsByProviderId(providerId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(mockReviewDTO);
        verify(reviewRepository).findByReviewedId(providerId);
        verify(reviewMapper).toDTO(mockReview);
    }

    @Test
    void getReviewsByProviderId_WhenNoReviews_ShouldReturnEmptyList() {
        Long providerId = 2L;
        
        when(reviewRepository.findByReviewedId(providerId)).thenReturn(Collections.emptyList());

        List<ReviewDTO> result = reviewService.getReviewsByProviderId(providerId);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(reviewRepository).findByReviewedId(providerId);
        verify(reviewMapper, never()).toDTO(any(Review.class));
    }

    @Test
    void getReviewsWithReviewerByProviderId_ShouldReturnReviewsWithReviewerList() {
        Long providerId = 2L;
        List<Review> mockReviews = Arrays.asList(mockReview);
        
        when(reviewRepository.findByReviewedId(providerId)).thenReturn(mockReviews);

        List<ReviewWithReviewerDTO> result = reviewService.getReviewsWithReviewerByProviderId(providerId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        
        ReviewWithReviewerDTO firstReview = result.get(0);
        assertThat(firstReview.getId()).isEqualTo(mockReview.getId());
        assertThat(firstReview.getNote()).isEqualTo(mockReview.getNote());
        assertThat(firstReview.getCommentaire()).isEqualTo(mockReview.getCommentaire());
        assertThat(firstReview.getReservationId()).isEqualTo(mockReview.getReservationId());
        assertThat(firstReview.getReviewerId()).isEqualTo(mockReview.getReviewer().getId());
        assertThat(firstReview.getReviewedId()).isEqualTo(mockReview.getReviewed().getId());
        assertThat(firstReview.getCreatedAt()).isEqualTo(mockReview.getCreatedAt());
        assertThat(firstReview.getReviewerFirstName()).isEqualTo(mockReview.getReviewer().getFirstName());
        
        verify(reviewRepository).findByReviewedId(providerId);
    }

    @Test
    void getReviewsWithReviewerByProviderId_WhenNoReviews_ShouldReturnEmptyList() {
        Long providerId = 2L;
        
        when(reviewRepository.findByReviewedId(providerId)).thenReturn(Collections.emptyList());

        List<ReviewWithReviewerDTO> result = reviewService.getReviewsWithReviewerByProviderId(providerId);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(reviewRepository).findByReviewedId(providerId);
    }

    @Test
    void getReviewsWithReviewerByProviderId_WithMultipleReviews_ShouldReturnAllReviews() {
        Long providerId = 2L;
        
        Review review1 = createMockReview();
        review1.setId(1L);
        review1.setNote(5);
        review1.setCommentaire("Excellent service");
        
        Review review2 = createMockReview();
        review2.setId(2L);
        review2.setNote(4);
        review2.setCommentaire("Très bon service");
        
        User reviewer1 = createMockUser();
        reviewer1.setId(1L);
        reviewer1.setFirstName("Jean");
        
        User reviewer2 = createMockUser();
        reviewer2.setId(3L);
        reviewer2.setFirstName("Marie");
        
        review1.setReviewer(reviewer1);
        review2.setReviewer(reviewer2);
        
        List<Review> mockReviews = Arrays.asList(review1, review2);
        
        when(reviewRepository.findByReviewedId(providerId)).thenReturn(mockReviews);

        List<ReviewWithReviewerDTO> result = reviewService.getReviewsWithReviewerByProviderId(providerId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNote()).isEqualTo(5);
        assertThat(result.get(0).getReviewerFirstName()).isEqualTo("Jean");
        
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getNote()).isEqualTo(4);
        assertThat(result.get(1).getReviewerFirstName()).isEqualTo("Marie");
        
        verify(reviewRepository).findByReviewedId(providerId);
    }

    @Test
    void getReviewsByReservationId_ShouldReturnReviewsList() {
        Long reservationId = 1L;
        List<Review> mockReviews = Arrays.asList(mockReview);
        
        when(reviewRepository.findByReservationId(reservationId)).thenReturn(mockReviews);
        when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);

        List<ReviewDTO> result = reviewService.getReviewsByReservationId(reservationId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(mockReviewDTO);
        verify(reviewRepository).findByReservationId(reservationId);
        verify(reviewMapper).toDTO(mockReview);
    }

    @Test
    void getReviewsByReservationId_WhenNoReviews_ShouldReturnEmptyList() {
        Long reservationId = 1L;
        
        when(reviewRepository.findByReservationId(reservationId)).thenReturn(Collections.emptyList());

        List<ReviewDTO> result = reviewService.getReviewsByReservationId(reservationId);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(reviewRepository).findByReservationId(reservationId);
        verify(reviewMapper, never()).toDTO(any(Review.class));
    }

    @Test
    void getReviewsByReservationId_WithMultipleReviews_ShouldReturnAllReviews() {
        Long reservationId = 1L;
        
        Review review1 = createMockReview();
        review1.setId(1L);
        review1.setNote(5);
        
        Review review2 = createMockReview();
        review2.setId(2L);
        review2.setNote(4);
        
        List<Review> mockReviews = Arrays.asList(review1, review2);
        List<ReviewDTO> mockReviewDTOs = Arrays.asList(
            createMockReviewDTO(),
            createMockReviewDTO()
        );
        
        when(reviewRepository.findByReservationId(reservationId)).thenReturn(mockReviews);
        when(reviewMapper.toDTO(review1)).thenReturn(mockReviewDTOs.get(0));
        when(reviewMapper.toDTO(review2)).thenReturn(mockReviewDTOs.get(1));

        List<ReviewDTO> result = reviewService.getReviewsByReservationId(reservationId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(mockReviewDTOs);
        verify(reviewRepository).findByReservationId(reservationId);
        verify(reviewMapper).toDTO(review1);
        verify(reviewMapper).toDTO(review2);
    }

    @Test
    void createReview_WithMinimalData_ShouldCreateReviewSuccessfully() {
        Long currentUserId = 1L;
        ReviewDTO minimalReviewDTO = createMockReviewDTO();
        minimalReviewDTO.setCommentaire(null); // Pas de commentaire
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(minimalReviewDTO.getReservationId())).thenReturn(Optional.of(mockReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(minimalReviewDTO.getReservationId(), currentUserId)).thenReturn(false);
            when(reviewMapper.toEntity(any(ReviewDTO.class))).thenReturn(mockReview);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(reviewMapper.toDTO(mockReview)).thenReturn(minimalReviewDTO);
            
            ReviewDTO result = reviewService.createReview(minimalReviewDTO);
            
            assertNotNull(result);
            assertEquals(minimalReviewDTO, result);
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(minimalReviewDTO.getReservationId());
            verify(reviewRepository).existsByReservationIdAndReviewerId(minimalReviewDTO.getReservationId(), currentUserId);
            verify(reviewMapper).toEntity(any(ReviewDTO.class));
            verify(reviewRepository).save(any(Review.class));
            verify(reviewMapper).toDTO(mockReview);
        }
    }

    @Test
    void createReview_WithBoundaryNoteValues_ShouldCreateReviewSuccessfully() {
        Long currentUserId = 1L;
        
        // Test avec note = 1
        ReviewDTO reviewDTO1 = createMockReviewDTO();
        reviewDTO1.setNote(1);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(reviewDTO1.getReservationId())).thenReturn(Optional.of(mockReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(reviewDTO1.getReservationId(), currentUserId)).thenReturn(false);
            when(reviewMapper.toEntity(any(ReviewDTO.class))).thenReturn(mockReview);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(reviewMapper.toDTO(mockReview)).thenReturn(reviewDTO1);
            
            ReviewDTO result = reviewService.createReview(reviewDTO1);
            
            assertNotNull(result);
            assertEquals(1, result.getNote());
        }
        
        // Test avec note = 5
        ReviewDTO reviewDTO5 = createMockReviewDTO();
        reviewDTO5.setNote(5);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(reviewDTO5.getReservationId())).thenReturn(Optional.of(mockReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(reviewDTO5.getReservationId(), currentUserId)).thenReturn(false);
            when(reviewMapper.toEntity(any(ReviewDTO.class))).thenReturn(mockReview);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(reviewMapper.toDTO(mockReview)).thenReturn(reviewDTO5);
            
            ReviewDTO result = reviewService.createReview(reviewDTO5);
            
            assertNotNull(result);
            assertEquals(5, result.getNote());
        }
    }

    private Review createMockReview() {
        Review review = new Review();
        review.setId(1L);
        review.setNote(5);
        review.setCommentaire("Excellent service");
        review.setReviewer(mockUser);
        review.setReviewed(mockUser);
        review.setReservationId(1L);
        review.setCreatedAt(LocalDateTime.now());
        return review;
    }

    private ReviewDTO createMockReviewDTO() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(1L);
        dto.setNote(5);
        dto.setCommentaire("Excellent service");
        dto.setReviewerId(1L);
        dto.setReviewedId(2L);
        dto.setReservationId(1L);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    private User createMockUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@test.com");
        user.setUserRole(UserRole.CLIENT);
        return user;
    }

    private Reservation createMockReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        
        User client = createMockUser();
        client.setId(1L);
        User provider = createMockUser();
        provider.setId(2L);
        
        reservation.setClient(client);
        reservation.setProvider(provider);
        reservation.setStatus(com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus.CLOSED);
        return reservation;
    }
}
