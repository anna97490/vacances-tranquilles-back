package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.exception.InvalidReservationStatusException;
import com.mastere_project.vacances_tranquilles.exception.InvalidReservationStatusTransitionException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UnauthorizedReservationAccessException;

import java.util.List;

/**
 * Interface du service de gestion des réservations.
 * Définit les opérations métier pour la gestion des réservations
 * incluant la récupération, le filtrage et la modification des statuts.
 * 
 * @author Mastere Project Team
 * @version 1.0
 */
public interface ReservationService {
    /**
     * Récupère toutes les réservations d'un utilisateur donné.
     * L'utilisateur peut être soit client soit prestataire.
     *
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des réservations de l'utilisateur
     */
    List<ReservationDTO> getReservationsForUserId(Long userId);

    /**
     * Récupère une réservation spécifique par son identifiant et l'identifiant de l'utilisateur.
     * Vérifie que l'utilisateur a accès à cette réservation (client ou prestataire).
     *
     * @param id L'identifiant de la réservation
     * @param userId L'identifiant de l'utilisateur
     * @return La réservation si trouvée et accessible
     * @throws ReservationNotFoundException Si la réservation n'existe pas ou si l'utilisateur n'y a pas accès
     */
    ReservationDTO getReservationByIdAndUserId(Long id, Long userId);

    /**
     * Récupère les réservations d'un utilisateur filtrées par statut.
     * L'utilisateur peut être soit client soit prestataire.
     *
     * @param userId L'identifiant de l'utilisateur
     * @param status Le statut de réservation à filtrer (PENDING, IN_PROGRESS, CLOSED)
     * @return Liste des réservations filtrées par statut
     * @throws InvalidReservationStatusException Si le statut fourni est invalide
     */
    List<ReservationDTO> getReservationsByStatus(Long userId, String status);

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
    ReservationDTO getReservationByIdAndUserIdAndStatus(Long id, Long userId, String status);

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
    ReservationDTO acceptReservationByProvider(Long reservationId, Long providerId);

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
    ReservationDTO completeReservationByProvider(Long reservationId, Long providerId);

    //ReservationDTO createReservation(ReservationCreateDTO dto);
}
