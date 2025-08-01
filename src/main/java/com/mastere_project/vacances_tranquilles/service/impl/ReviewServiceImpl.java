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
import java.util.ArrayList;
import java.util.List;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotCompletedException;
import com.mastere_project.vacances_tranquilles.exception.InvalidReviewUserException;
import com.mastere_project.vacances_tranquilles.exception.ReviewNotFoundException;

/**
 * Service métier responsable de la gestion des avis entre utilisateurs.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String USER_NOT_FOUND_MESSAGE = "Utilisateur non trouvé";
    private static final String USER_ROLE_NULL_MESSAGE = "Rôle utilisateur non défini en base de données";
    private static final String TARGET_USER_NOT_FOUND_MESSAGE = "Utilisateur cible non trouvé";
    private static final String OWN_REVIEWS_ONLY_MESSAGE = "Vous ne pouvez consulter que vos propres avis";

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
        // Vérification de l'utilisateur authentifié
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // Vérification en base de données que l'utilisateur existe
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AccessDeniedException(USER_NOT_FOUND_MESSAGE));

        // Vérification que le rôle de l'utilisateur en base correspond au rôle authentifié
        if (user.getUserRole() == null) {
            throw new AccessDeniedException(USER_ROLE_NULL_MESSAGE);
        }

        // Vérification que l'utilisateur authentifié est bien le reviewer
        if (!currentUserId.equals(dto.getReviewerId())) {
            throw new InvalidReviewUserException("Vous ne pouvez créer un avis qu'en votre nom");
        }

        if (dto.getNote() < 1 || dto.getNote() > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 1 et 5.");
        }

        Reservation reservation = reservationRepository.findById(dto.getReservationId()).orElse(null);
        if (reservation == null) {
            throw new ReservationNotFoundException("Réservation introuvable");
        }

        if (reservation.getStatus() == null || !reservation.getStatus().equals("CLOSED")) {
            throw new ReservationNotCompletedException("La réservation doit avoir le statut CLOSED pour pouvoir écrire un avis");
        }

        boolean valid = (reservation.getClientId() != null && reservation.getClientId().equals(dto.getReviewerId())
                && reservation.getProviderId() != null && reservation.getProviderId().equals(dto.getReviewedId()))
                || (reservation.getProviderId() != null && reservation.getProviderId().equals(dto.getReviewerId())
                && reservation.getClientId() != null && reservation.getClientId().equals(dto.getReviewedId()));

        if (!valid) {
            throw new InvalidReviewUserException("Les utilisateurs ne correspondent pas à la réservation");
        }

        Review review = reviewMapper.toEntity(dto);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        
        return reviewMapper.toDTO(saved);
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
        List<ReviewDTO> dtos = new ArrayList<>();
        
        for (Review review : reviews) {
            dtos.add(reviewMapper.toDTO(review));
        }
        
        return dtos;
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
        List<ReviewDTO> dtos = new ArrayList<>();
        for (Review review : reviews) {
            dtos.add(reviewMapper.toDTO(review));
        }
        return dtos;
    }
}
