package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.UpdateReservationStatusDTO;
import com.mastere_project.vacances_tranquilles.exception.InvalidReservationStatusTransitionException;
import com.mastere_project.vacances_tranquilles.exception.MissingReservationDataException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ServiceNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UnauthorizedReservationAccessException;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface du service de gestion des réservations.
 * Définit les opérations métier pour la gestion des réservations
 * incluant la récupération et la modification des statuts.
 * Le rôle de l'utilisateur est automatiquement déterminé côté serveur.
 * Cette interface centralise la logique métier pour assurer la cohérence des opérations.
 * 
 * @author VacancesTranquilles
 * @version 1.0
 * @since 1.0
 */
public interface ReservationService {
    /**
     * Récupère toutes les réservations de l'utilisateur authentifié.
     * Le rôle est automatiquement déterminé en vérifiant l'utilisateur en base de données.
     * Récupère les réservations en fonction du rôle de l'utilisateur :
     * - CLIENT : récupère toutes les réservations où l'utilisateur est le client
     * - PROVIDER : récupère toutes les réservations où l'utilisateur est le prestataire
     * Le système vérifie automatiquement l'authentification et l'autorisation.
     *
     * @return Liste des réservations de l'utilisateur selon son rôle
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'est pas autorisé
     * @throws UserNotFoundException si l'utilisateur n'existe pas en base
     */
    List<ReservationResponseDTO> getAllReservations();

    /**
     * Récupère une réservation spécifique par son identifiant.
     * Vérifie que l'utilisateur authentifié a accès à cette réservation (client ou prestataire).
     * Le rôle est automatiquement déterminé côté serveur.
     * Le système vérifie l'autorisation en comparant l'utilisateur avec les participants de la réservation.
     *
     * @param id L'identifiant de la réservation
     * @return La réservation si trouvée et accessible
     * @throws ReservationNotFoundException Si la réservation n'existe pas ou si l'utilisateur n'y a pas accès
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'est pas autorisé
     * @throws UserNotFoundException si l'utilisateur n'existe pas en base
     */
    ReservationResponseDTO getReservationById(Long id);

    /**
     * Permet à un prestataire de changer le statut d'une réservation.
     * Change le statut selon les transitions autorisées : PENDING → IN_PROGRESS → CLOSED ou PENDING → CANCELLED
     * Le rôle est automatiquement déterminé côté serveur.
     * Le système vérifie que l'utilisateur est bien le prestataire de la réservation.
     *
     * @param reservationId L'identifiant de la réservation à modifier
     * @param dto Les données de mise à jour du statut
     * @return La réservation mise à jour avec le nouveau statut
     * @throws ReservationNotFoundException Si la réservation n'existe pas
     * @throws InvalidReservationStatusTransitionException Si la réservation n'est pas dans un statut permettant la transition
     * @throws UnauthorizedReservationAccessException Si le prestataire n'est pas autorisé à modifier cette réservation
     * @throws UserNotFoundException si l'utilisateur n'existe pas en base
     */
    ReservationResponseDTO changeStatusOfReservationByProvider(Long reservationId, UpdateReservationStatusDTO dto);

    /**
     * Crée une nouvelle réservation.
     * L'utilisateur authentifié doit être le client de la réservation.
     * La réservation est créée avec le statut PENDING.
     * Le système vérifie automatiquement l'autorisation et valide les données.
     *
     * @param dto Les données de création de la réservation
     * @return La réservation créée
     * @throws UnauthorizedReservationAccessException Si l'utilisateur n'est pas autorisé à créer cette réservation
     * @throws MissingReservationDataException si des données requises sont manquantes
     * @throws ServiceNotFoundException si le service spécifié n'existe pas
     */
    ReservationResponseDTO createReservation(ReservationDTO dto);
}
