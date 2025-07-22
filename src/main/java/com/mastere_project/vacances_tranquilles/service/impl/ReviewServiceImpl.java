package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;
import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.mapper.ReviewMapper;
import com.mastere_project.vacances_tranquilles.repository.ReviewRepository;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.service.ReviewService;
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

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Constructeur du service d'avis.
     *
     * @param reviewRepository      le repository pour les entités Review
     * @param reservationRepository le repository pour les entités Reservation
     * @param reviewMapper          le mapper pour convertir entre entités et DTO Review
     */
    public ReviewServiceImpl(ReviewRepository reviewRepository, ReservationRepository reservationRepository,
                             ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Crée un avis utilisateur après vérification des conditions de validité.
     *
     * @param dto les données de l'avis à enregistrer
     * @return le DTO de l'avis enregistré
     * @throws IllegalArgumentException           si la note est en dehors des bornes autorisées
     * @throws ReservationNotFoundException       si la réservation n'existe pas
     * @throws ReservationNotCompletedException   si la réservation n'est pas terminée
     * @throws InvalidReviewUserException         si les utilisateurs ne correspondent pas à la réservation
     */
    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO dto) {
        if (dto.getNote() < 1 || dto.getNote() > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 1 et 5.");
        }

        Reservation reservation = reservationRepository.findById(dto.getReservationId()).orElse(null);
        if (reservation == null) {
            throw new ReservationNotFoundException("Réservation introuvable");
        }

        if (reservation.getStatus() == null || !reservation.getStatus().equalsIgnoreCase("completed")) {
            throw new ReservationNotCompletedException("La réservation n'est pas terminée");
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
     * Récupère tous les avis reçus par un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur évalué
     * @return la liste des avis reçus
     */
    @Override
    public List<ReviewDTO> getReviewsForUser(Long userId) {
        List<Review> reviews = reviewRepository.findByReviewedId(userId);
        List<ReviewDTO> dtos = new ArrayList<>();
        for (Review review : reviews) {
            dtos.add(reviewMapper.toDTO(review));
        }
        return dtos;
    }

    /**
     * Récupère un avis spécifique à partir de son identifiant.
     *
     * @param reviewId l'identifiant de l'avis
     * @return le DTO de l'avis
     * @throws ReviewNotFoundException si aucun avis n’est trouvé
     */
    @Override
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new ReviewNotFoundException("Avis introuvable");
        }
        return reviewMapper.toDTO(review);
    }

    /**
     * Récupère tous les avis rédigés par un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur rédacteur
     * @return la liste des avis rédigés
     */
    @Override
    public List<ReviewDTO> getReviewsWrittenByUser(Long userId) {
        List<Review> reviews = reviewRepository.findByReviewerId(userId);
        List<ReviewDTO> dtos = new ArrayList<>();
        for (Review review : reviews) {
            dtos.add(reviewMapper.toDTO(review));
        }
        return dtos;
    }

    /**
     * Récupère tous les avis rédigés par un utilisateur donné à destination d’un autre utilisateur.
     *
     * @param reviewerId identifiant de l’évaluateur
     * @param reviewedId identifiant de l’évalué
     * @return la liste des avis correspondants
     */
    @Override
    public List<ReviewDTO> getReviewsFromUserToUser(Long reviewerId, Long reviewedId) {
        List<Review> reviews = reviewRepository.findByReviewerIdAndReviewedId(reviewerId, reviewedId);
        List<ReviewDTO> dtos = new ArrayList<>();
        for (Review review : reviews) {
            dtos.add(reviewMapper.toDTO(review));
        }
        return dtos;
    }
}
