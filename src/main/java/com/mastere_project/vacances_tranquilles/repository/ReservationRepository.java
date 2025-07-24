package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'accès aux données des réservations.
 * Fournit des méthodes de requête personnalisées pour la recherche
 * de réservations par client, prestataire et statut.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Trouve toutes les réservations où l'utilisateur est soit client soit prestataire.
     *
     * @param clientId L'identifiant du client
     * @param providerId L'identifiant du prestataire
     * @return Liste des réservations où l'utilisateur est impliqué
     */
    List<Reservation> findByClientIdOrProviderId(Long clientId, Long providerId);

    /**
     * Trouve une réservation spécifique par son identifiant et vérifie que l'utilisateur y a accès.
     * L'utilisateur doit être soit le client soit le prestataire de la réservation.
     *
     * @param id1 L'identifiant de la réservation (premier paramètre)
     * @param clientId L'identifiant du client
     * @param id2 L'identifiant de la réservation (deuxième paramètre)
     * @param providerId L'identifiant du prestataire
     * @return Optional contenant la réservation si trouvée et accessible
     */
    Optional<Reservation> findByIdAndClientIdOrIdAndProviderId(Long id1, Long clientId, Long id2, Long providerId);

    /**
     * Trouve toutes les réservations d'un client avec un statut spécifique.
     *
     * @param status Le statut de réservation à filtrer
     * @param clientId L'identifiant du client
     * @return Liste des réservations du client avec le statut spécifié
     */
    List<Reservation> findByStatusAndClientId(ReservationStatus status, Long clientId);

    /**
     * Trouve toutes les réservations d'un prestataire avec un statut spécifique.
     *
     * @param status Le statut de réservation à filtrer
     * @param providerId L'identifiant du prestataire
     * @return Liste des réservations du prestataire avec le statut spécifié
     */
    List<Reservation> findByStatusAndProviderId(ReservationStatus status, Long providerId);

    /**
     * Trouve une réservation spécifique d'un client avec un statut spécifique.
     *
     * @param id L'identifiant de la réservation
     * @param status Le statut de réservation attendu
     * @param clientId L'identifiant du client
     * @return Optional contenant la réservation si trouvée
     */
    Optional<Reservation> findByIdAndStatusAndClientId(Long id, ReservationStatus status, Long clientId);

    /**
     * Trouve une réservation spécifique d'un prestataire avec un statut spécifique.
     *
     * @param id L'identifiant de la réservation
     * @param status Le statut de réservation attendu
     * @param providerId L'identifiant du prestataire
     * @return Optional contenant la réservation si trouvée
     */
    Optional<Reservation> findByIdAndStatusAndProviderId(Long id, ReservationStatus status, Long providerId);

}
