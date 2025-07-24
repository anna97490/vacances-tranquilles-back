package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.exception.InvalidReservationStatusException;
import com.mastere_project.vacances_tranquilles.exception.InvalidReservationStatusTransitionException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UnauthorizedReservationAccessException;
import com.mastere_project.vacances_tranquilles.mapper.ReservationMapper;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Implémentation du service de gestion des réservations.
 * Fournit les opérations métier pour la gestion des réservations
 * incluant la récupération, le filtrage et la modification des statuts.
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    /**
     * Repository pour l'accès aux données des réservations.
     */
    private final ReservationRepository reservationRepository;
    
    /**
     * Mapper pour la conversion entre entités et DTOs de réservation.
     */
    private final ReservationMapper reservationMapper;

    /**
     * Récupère toutes les réservations d'un utilisateur donné.
     * L'utilisateur peut être soit client soit prestataire.
     *
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des réservations de l'utilisateur
     */
    @Override
    public List<ReservationDTO> getReservationsForUserId(Long userId) {
        return reservationRepository.findByClientIdOrProviderId(userId, userId)
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    /**
     * Récupère une réservation spécifique par son identifiant et l'identifiant de l'utilisateur.
     * Vérifie que l'utilisateur a accès à cette réservation (client ou prestataire).
     *
     * @param id L'identifiant de la réservation
     * @param userId L'identifiant de l'utilisateur
     * @return La réservation si trouvée et accessible
     * @throws ReservationNotFoundException Si la réservation n'existe pas ou si l'utilisateur n'y a pas accès
     */
    @Override
    public ReservationDTO getReservationByIdAndUserId(Long id, Long userId) {
        return reservationRepository
                .findByIdAndClientIdOrIdAndProviderId(id, userId, id, userId)
                .map(reservationMapper::toDTO)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation introuvable ou non autorisée."));
    }

    /**
     * Récupère les réservations d'un utilisateur filtrées par statut.
     * L'utilisateur peut être soit client soit prestataire.
     *
     * @param userId L'identifiant de l'utilisateur
     * @param status Le statut de réservation à filtrer (PENDING, IN_PROGRESS, CLOSED)
     * @return Liste des réservations filtrées par statut
     * @throws InvalidReservationStatusException Si le statut fourni est invalide
     */
    @Override
    public List<ReservationDTO> getReservationsByStatus(Long userId, String status) {
        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());

            List<Reservation> asClient = reservationRepository.findByStatusAndClientId(reservationStatus, userId);
            List<Reservation> asProvider = reservationRepository.findByStatusAndProviderId(reservationStatus, userId);

            return Stream.concat(asClient.stream(), asProvider.stream())
                    .map(reservationMapper::toDTO)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidReservationStatusException("Statut de réservation invalide : " + status);
        }
    }

    /**
     * Récupère une réservation spécifique par son identifiant, l'identifiant de l'utilisateur et le statut.
     * Vérifie que l'utilisateur a accès à cette réservation et que le statut correspond.
     *
     * @param id L'identifiant de la réservation
     * @param userId L'identifiant de l'utilisateur
     * @param status Le statut attendu de la réservation
     * @return La réservation si trouvée, accessible et avec le bon statut
     * @throws ReservationNotFoundException Si la réservation n'existe pas
     * @throws UnauthorizedReservationAccessException Si l'utilisateur n'a pas accès à cette réservation
     * @throws InvalidReservationStatusTransitionException Si le statut de la réservation ne correspond pas
     * @throws InvalidReservationStatusException Si le statut fourni est invalide
     */
    @Override
    public ReservationDTO getReservationByIdAndUserIdAndStatus(Long id, Long userId, String status) {
        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationNotFoundException("Réservation introuvable"));

            if (!reservation.getClient().getId().equals(userId) && !reservation.getProvider().getId().equals(userId)) {
                throw new UnauthorizedReservationAccessException("Vous n'avez pas accès à cette réservation");
            }

            if (reservation.getStatus() != reservationStatus) {
                throw new InvalidReservationStatusTransitionException("Le statut de la réservation ne correspond pas à celui demandé");
            }

            return reservationMapper.toDTO(reservation);

        } catch (IllegalArgumentException e) {
            throw new InvalidReservationStatusException("Statut de réservation invalide : " + status);
        }
    }

    /**
     * Permet à un prestataire d'accepter une réservation en attente.
     * Change le statut de PENDING à IN_PROGRESS.
     *
     * @param reservationId L'identifiant de la réservation à accepter
     * @param providerId L'identifiant du prestataire qui accepte la réservation
     * @return La réservation mise à jour avec le nouveau statut
     * @throws ReservationNotFoundException Si la réservation n'existe pas
     * @throws InvalidReservationStatusTransitionException Si la réservation n'est pas en statut PENDING
     * @throws UnauthorizedReservationAccessException Si le prestataire n'est pas autorisé à accepter cette réservation
     */
    @Override
    public ReservationDTO acceptReservationByProvider(Long reservationId, Long providerId) {
        Reservation reservation = findReservationWithStatusOrThrow(reservationId, ReservationStatus.PENDING);

        if (!reservation.getProvider().getId().equals(providerId)) {
            throw new UnauthorizedReservationAccessException("Vous n'êtes pas autorisé à accepter cette réservation");
        }

        reservation.setStatus(ReservationStatus.IN_PROGRESS);
        Reservation updated = reservationRepository.save(reservation);
        return reservationMapper.toDTO(updated);
    }

    /**
     * Permet à un prestataire de clôturer une réservation en cours.
     * Change le statut de IN_PROGRESS à CLOSED.
     *
     * @param reservationId L'identifiant de la réservation à clôturer
     * @param providerId L'identifiant du prestataire qui clôture la réservation
     * @return La réservation mise à jour avec le nouveau statut
     * @throws ReservationNotFoundException Si la réservation n'existe pas
     * @throws InvalidReservationStatusTransitionException Si la réservation n'est pas en statut IN_PROGRESS
     * @throws UnauthorizedReservationAccessException Si le prestataire n'est pas autorisé à clôturer cette réservation
     */
    @Override
    public ReservationDTO completeReservationByProvider(Long reservationId, Long providerId) {
        Reservation reservation = findReservationWithStatusOrThrow(reservationId, ReservationStatus.IN_PROGRESS);

        if (!reservation.getProvider().getId().equals(providerId)) {
            throw new UnauthorizedReservationAccessException("Vous n'êtes pas autorisé à clôturer cette réservation");
        }

        reservation.setStatus(ReservationStatus.CLOSED);
        Reservation updated = reservationRepository.save(reservation);
        return reservationMapper.toDTO(updated);
    }

    /**
     * Méthode utilitaire pour trouver une réservation et vérifier son statut.
     * Lance une exception si la réservation n'existe pas ou si le statut ne correspond pas.
     *
     * @param id L'identifiant de la réservation
     * @param expectedStatus Le statut attendu de la réservation
     * @return La réservation si trouvée et avec le bon statut
     * @throws ReservationNotFoundException Si la réservation n'existe pas
     * @throws InvalidReservationStatusTransitionException Si le statut de la réservation ne correspond pas
     */
    private Reservation findReservationWithStatusOrThrow(Long id, ReservationStatus expectedStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation introuvable"));

        if (reservation.getStatus() != expectedStatus) {
            throw new InvalidReservationStatusTransitionException("Le statut actuel ne correspond pas à celui attendu. Attendu : " + expectedStatus + ", trouvé : " + reservation.getStatus());
        }

        return reservation;
    }
}        
