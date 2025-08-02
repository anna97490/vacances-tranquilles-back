package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.InvalidReviewUserException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotCompletedException;
import com.mastere_project.vacances_tranquilles.exception.ReviewNotFoundException;
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
import java.util.List;
import java.util.Optional;

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
            when(reviewMapper.toEntity(any(ReviewDTO.class))).thenReturn(mockReview);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);
            
            ReviewDTO result = reviewService.createReview(mockReviewDTO);
            
            assertNotNull(result);
            assertEquals(mockReviewDTO, result);
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
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
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenReviewerIdDoesNotMatchCurrentUser_ShouldThrowException() {
        Long currentUserId = 1L;
        mockReviewDTO.setReviewerId(2L); // Different from current user
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            
            InvalidReviewUserException exception = assertThrows(InvalidReviewUserException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            assertEquals("Vous ne pouvez créer un avis qu'en votre nom", exception.getMessage());
            
            verify(userRepository).findById(currentUserId);
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
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenReservationStatusNotClosed_ShouldThrowException() {
        Long currentUserId = 1L;
        mockReservation.setStatus("IN_PROGRESS");
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(mockReviewDTO.getReservationId())).thenReturn(Optional.of(mockReservation));
            
            ReservationNotCompletedException exception = assertThrows(ReservationNotCompletedException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            assertEquals("La réservation doit avoir le statut CLOSED pour pouvoir écrire un avis", exception.getMessage());
            
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
            verify(reviewRepository, never()).save(any());
        }
    }

    @Test
    void createReview_WhenReviewAlreadyExists_ShouldReturnExistingReview() {
        Long currentUserId = 1L;
        mockReviewDTO.setReviewerId(1L); // Same reviewer as mockReview
        mockReviewDTO.setReviewedId(2L);
        mockReviewDTO.setReservationId(1L);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(mockReviewDTO.getReservationId())).thenReturn(Optional.of(mockReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(
                mockReviewDTO.getReservationId(), currentUserId))
                .thenReturn(true);
            when(reviewRepository.findByReservationIdAndReviewerId(
                mockReviewDTO.getReservationId(), currentUserId))
                .thenReturn(Optional.of(mockReview));
            when(reviewMapper.toDTO(mockReview)).thenReturn(mockReviewDTO);

            ReviewDTO result = reviewService.createReview(mockReviewDTO);

            assertNotNull(result);
            assertEquals(mockReviewDTO, result);

            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
            verify(reviewRepository).existsByReservationIdAndReviewerId(
                mockReviewDTO.getReservationId(), currentUserId);
            verify(reviewRepository).findByReservationIdAndReviewerId(
                mockReviewDTO.getReservationId(), currentUserId);
            verify(reviewMapper).toDTO(mockReview);
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

    // Tests pour les cas où l'utilisateur ne peut pas poster de review

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
        }
    }

    @Test
    void createReview_WhenUsersDoNotMatchReservation_ShouldThrowException() {
        Long currentUserId = 1L;
        ReviewDTO invalidReviewDTO = createMockReviewDTO();
        invalidReviewDTO.setReviewerId(1L);
        invalidReviewDTO.setReviewedId(3L); // Utilisateur qui n'est pas dans la réservation
        
        Reservation invalidReservation = createMockReservation();
        invalidReservation.setClientId(1L);
        invalidReservation.setProviderId(2L); // Différent de reviewedId (3L)
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(invalidReviewDTO.getReservationId())).thenReturn(Optional.of(invalidReservation));
            
            InvalidReviewUserException exception = assertThrows(InvalidReviewUserException.class, 
                () -> reviewService.createReview(invalidReviewDTO));
            
            assertEquals("Les utilisateurs ne correspondent pas à la réservation", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(invalidReviewDTO.getReservationId());
        }
    }

    // Tests pour les cas où la review n'existe pas

    @Test
    void getReviewById_WhenReviewDoesNotExist_ShouldThrowException() {
        Long currentUserId = 1L;
        Long nonExistentReviewId = 999L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reviewRepository.findById(nonExistentReviewId)).thenReturn(Optional.empty());
            
            ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class, 
                () -> reviewService.getReviewById(nonExistentReviewId));
            
            assertEquals("Avis introuvable", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reviewRepository).findById(nonExistentReviewId);
        }
    }

    @Test
    void createReview_WhenExistingReviewNotFound_ShouldThrowException() {
        Long currentUserId = 1L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(mockUser));
            when(reservationRepository.findById(mockReviewDTO.getReservationId())).thenReturn(Optional.of(mockReservation));
            when(reviewRepository.existsByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId)).thenReturn(true);
            when(reviewRepository.findByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId)).thenReturn(Optional.empty());
            
            ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class, 
                () -> reviewService.createReview(mockReviewDTO));
            
            assertEquals("Avis existant introuvable", exception.getMessage());
            verify(userRepository).findById(currentUserId);
            verify(reservationRepository).findById(mockReviewDTO.getReservationId());
            verify(reviewRepository).existsByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId);
            verify(reviewRepository).findByReservationIdAndReviewerId(mockReviewDTO.getReservationId(), currentUserId);
        }
    }

    // Tests pour les cas où l'utilisateur n'est pas autorisé

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
        reservation.setClientId(1L);
        reservation.setProviderId(2L);
        reservation.setStatus("CLOSED");
        return reservation;
    }
}
