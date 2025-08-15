package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.mapper.ReviewMapper;
import com.mastere_project.vacances_tranquilles.repository.ReviewRepository;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotCompletedException;
import com.mastere_project.vacances_tranquilles.exception.InvalidReviewUserException;
import com.mastere_project.vacances_tranquilles.exception.ReviewNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReviewAlreadyExistsException;

/**
 * Service métier responsable de la gestion des avis entre utilisateurs.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String USER_NOT_FOUND_MESSAGE = "Utilisateur non trouvé";
    private static final String USER_ROLE_NULL_MESSAGE = "Rôle utilisateur non défini en base de données";

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Constructeur du service d'avis.
     *
     * @param reviewRepository      le repository pour les entités Review
     * @param reservationRepository le repository pour les entités Reservation
     * @param userRepository        le repository pour les entités User
     * @param reviewMapper          le mapper pour convertir entre entités et DTO Review
     */
    public ReviewServiceImpl(ReviewRepository reviewRepository, ReservationRepository reservationRepository,
                             UserRepository userRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Crée un avis utilisateur après vérification des conditions de validité.
     *
     * @param dto les données de l'avis à enregistrer
     * @return le DTO de l'avis enregistré
     * @throws IllegalArgumentException           si la note est en dehors des bornes autorisées
     * @throws ReservationNotFoundException       si la réservation n'existe pas
     * @throws ReservationNotCompletedException   si la réservation n'est pas fermée (status != "closed")
     * @throws InvalidReviewUserException         si les utilisateurs ne correspondent pas à la réservation
     * @throws AccessDeniedException              si l'utilisateur n'est pas trouvé ou est anonymisé
     */
    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        validateUser(currentUserId);
        validateNote(dto.getNote());
        
        dto.setReviewerId(currentUserId);
        
        Reservation reservation = validateAndGetReservation(dto.getReservationId());
        validateReservationStatus(reservation);
        validateNoExistingReview(dto.getReservationId(), currentUserId);
        
        Long reviewedId = determineReviewedId(reservation, currentUserId);
        dto.setReviewedId(reviewedId);

        Review review = reviewMapper.toEntity(dto);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        
        return reviewMapper.toDTO(saved);
    }

    /**
     * Valide que l'utilisateur existe et a un rôle défini.
     *
     * @param currentUserId l'identifiant de l'utilisateur à valider
     * @throws AccessDeniedException si l'utilisateur n'existe pas ou n'a pas de rôle défini
     */
    private void validateUser(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AccessDeniedException(USER_NOT_FOUND_MESSAGE));
        
        if (user.getUserRole() == null) {
            throw new AccessDeniedException(USER_ROLE_NULL_MESSAGE);
        }
    }

    /**
     * Valide que la note est dans la plage autorisée (1-5).
     *
     * @param note la note à valider
     * @throws IllegalArgumentException si la note n'est pas dans la plage autorisée
     */
    private void validateNote(int note) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 1 et 5.");
        }
    }

    /**
     * Valide et récupère une réservation par son identifiant.
     *
     * @param reservationId l'identifiant de la réservation à récupérer
     * @return la réservation trouvée
     * @throws ReservationNotFoundException si la réservation n'existe pas
     */
    private Reservation validateAndGetReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        
        if (reservation == null) {
            throw new ReservationNotFoundException("Réservation introuvable");
        }

        return reservation;
    }

    /**
     * Valide que la réservation a le statut CLOSED requis pour créer un avis.
     *
     * @param reservation la réservation à valider
     * @throws ReservationNotCompletedException si la réservation n'a pas le statut CLOSED
     */
    private void validateReservationStatus(Reservation reservation) {
        if (reservation.getStatus() == null || !reservation.getStatus().equals(com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus.CLOSED)) {
            throw new ReservationNotCompletedException("La réservation doit avoir le statut CLOSED pour pouvoir écrire un avis");
        }
    }

    /**
     * Valide qu'aucun avis n'existe déjà pour cette réservation par cet utilisateur.
     *
     * @param reservationId l'identifiant de la réservation
     * @param currentUserId l'identifiant de l'utilisateur actuel
     * @throws ReviewAlreadyExistsException si un avis existe déjà pour cette réservation par cet utilisateur
     */
    private void validateNoExistingReview(Long reservationId, Long currentUserId) {
        if (reviewRepository.existsByReservationIdAndReviewerId(reservationId, currentUserId)) {
            throw new ReviewAlreadyExistsException("Vous avez déjà créé un avis pour cette réservation. Un seul avis par réservation est autorisé.");
        }
    }

    /**
     * Détermine l'identifiant de l'utilisateur évalué en fonction du rôle de l'utilisateur actuel dans la réservation.
     *
     * @param reservation la réservation contenant les utilisateurs
     * @param currentUserId l'identifiant de l'utilisateur actuel
     * @return l'identifiant de l'utilisateur évalué (client si l'actuel est provider, provider si l'actuel est client)
     * @throws InvalidReviewUserException si l'utilisateur actuel n'est pas impliqué dans la réservation
     */
    private Long determineReviewedId(Reservation reservation, Long currentUserId) {
        if (reservation.getClient() != null && reservation.getClient().getId().equals(currentUserId)) {
            return reservation.getProvider() != null ? reservation.getProvider().getId() : null;
        } else if (reservation.getProvider() != null && reservation.getProvider().getId().equals(currentUserId)) {
            return reservation.getClient() != null ? reservation.getClient().getId() : null;
        } else {
            throw new InvalidReviewUserException("Vous ne pouvez évaluer que les utilisateurs impliqués dans cette réservation");
        }
    }

    /**
     * Récupère un avis spécifique à partir de son identifiant.
     *
     * @param reviewId l'identifiant de l'avis
     * @return le DTO de l'avis
     * @throws ReviewNotFoundException si aucun avis n'est trouvé
     */
    @Override
    public ReviewDTO getReviewById(Long reviewId) {
        // Vérification de l'utilisateur authentifié
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérification en base de données que l'utilisateur existe
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AccessDeniedException(USER_NOT_FOUND_MESSAGE));

        // Vérification que le rôle de l'utilisateur en base correspond au rôle authentifié
        if (user.getUserRole() == null) {
            throw new AccessDeniedException(USER_ROLE_NULL_MESSAGE);
        }

        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new ReviewNotFoundException("Avis introuvable");
        }
        
        return reviewMapper.toDTO(review);
    }

    /**
     * Récupère tous les avis rédigés par l'utilisateur authentifié.
     *
     * @return la liste des avis rédigés par l'utilisateur authentifié
     */
    @Override
    public List<ReviewDTO> getReviewsWrittenByUser() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérification en base de données que l'utilisateur existe
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AccessDeniedException(USER_NOT_FOUND_MESSAGE));

        // Vérification que le rôle de l'utilisateur en base correspond au rôle authentifié
        if (user.getUserRole() == null) {
            throw new AccessDeniedException(USER_ROLE_NULL_MESSAGE);
        }

        List<Review> reviews = reviewRepository.findByReviewerId(currentUserId);
        return reviews.stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    /**
     * Récupère tous les avis reçus par l'utilisateur authentifié.
     *
     * @return la liste des avis reçus
     */
    @Override
    public List<ReviewDTO> getReviewsReceivedByUser() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérification en base de données que l'utilisateur existe
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AccessDeniedException(USER_NOT_FOUND_MESSAGE));

        // Vérification que le rôle de l'utilisateur en base correspond au rôle authentifié
        if (user.getUserRole() == null) {
            throw new AccessDeniedException(USER_ROLE_NULL_MESSAGE);
        }

        List<Review> reviews = reviewRepository.findByReviewedId(currentUserId);
        return reviews.stream()
                .map(reviewMapper::toDTO)
                .toList();
    }
}