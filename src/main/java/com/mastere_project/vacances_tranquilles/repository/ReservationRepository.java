package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'accès aux données des réservations.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Trouve toutes les réservations où l'utilisateur est soit client soit prestataire.
     * Cette méthode permet de récupérer toutes les réservations impliquant un utilisateur donné,
     * peu importe son rôle (client ou prestataire).
     *
     * @param clientId L'identifiant du client
     * @param providerId L'identifiant du prestataire
     * @return Liste des réservations où l'utilisateur est impliqué
     * @throws IllegalArgumentException si les paramètres sont null
     */
    List<Reservation> findByClientIdOrProviderId(Long clientId, Long providerId);

    /**
     * Trouve toutes les réservations d'un client.
     * Récupère toutes les réservations où l'utilisateur spécifié est le client.
     *
     * @param clientId L'identifiant du client
     * @return Liste des réservations du client
     * @throws IllegalArgumentException si clientId est null
     */
    List<Reservation> findByClientId(Long clientId);

    /**
     * Trouve toutes les réservations d'un prestataire.
     * Récupère toutes les réservations où l'utilisateur spécifié est le prestataire.
     *
     * @param providerId L'identifiant du prestataire
     * @return Liste des réservations du prestataire
     * @throws IllegalArgumentException si providerId est null
     */
    List<Reservation> findByProviderId(Long providerId);

    /**
     * Trouve toutes les réservations d'un client avec un statut spécifique.
     * Permet de filtrer les réservations d'un client par statut (PENDING, IN_PROGRESS, CLOSED).
     *
     * @param status Le statut de réservation à filtrer
     * @param clientId L'identifiant du client
     * @return Liste des réservations du client avec le statut spécifié
     * @throws IllegalArgumentException si status ou clientId sont null
     */
    List<Reservation> findByStatusAndClientId(ReservationStatus status, Long clientId);

    /**
     * Trouve toutes les réservations d'un prestataire avec un statut spécifique.
     * Permet de filtrer les réservations d'un prestataire par statut (PENDING, IN_PROGRESS, CLOSED).
     *
     * @param status Le statut de réservation à filtrer
     * @param providerId L'identifiant du prestataire
     * @return Liste des réservations du prestataire avec le statut spécifié
     * @throws IllegalArgumentException si status ou providerId sont null
     */
    List<Reservation> findByStatusAndProviderId(ReservationStatus status, Long providerId);

    /**
     * Trouve toutes les réservations avec un statut spécifique.
     * Récupère toutes les réservations ayant le statut spécifié, peu importe le client ou prestataire.
     *
     * @param status Le statut de réservation à filtrer
     * @return Liste des réservations avec le statut spécifié
     * @throws IllegalArgumentException si status est null
     */
    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * Trouve une réservation spécifique par son identifiant et vérifie que l'utilisateur y a accès.
     * L'utilisateur doit être soit le client soit le prestataire de la réservation.
     * Cette méthode assure la sécurité en vérifiant l'autorisation d'accès.
     *
     * @param id L'identifiant de la réservation
     * @param userId L'identifiant de l'utilisateur (client ou prestataire)
     * @return Optional contenant la réservation si trouvée et accessible
     * @throws IllegalArgumentException si id ou userId sont null
     */
    @Query("SELECT r FROM Reservation r WHERE r.id = :id AND (r.client.id = :userId OR r.provider.id = :userId)")
    Optional<Reservation> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
