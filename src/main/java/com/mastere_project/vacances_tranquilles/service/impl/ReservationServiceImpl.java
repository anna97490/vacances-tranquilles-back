package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.exception.InvalidReservationStatusTransitionException;
import com.mastere_project.vacances_tranquilles.exception.MissingReservationDataException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ServiceNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UnauthorizedReservationAccessException;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.ReservationMapper;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implémentation du service de gestion des réservations.
 * Fournit les opérations métier pour la gestion des réservations
 * incluant la récupération, le filtrage et la modification des statuts.
 * Cette implémentation gère l'authentification et l'autorisation automatiquement.
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final String USER_NOT_FOUND_MESSAGE = "Utilisateur introuvable";

    private static final String UNAUTHORIZED_MESSAGE = "Vous n'êtes pas autorisé à accéder à cette réservation";

    /**
     * Repository pour l'accès aux données des réservations.
     */
    private final ReservationRepository reservationRepository;

    /**
     * Repository pour l'accès aux données des utilisateurs.
     */
    private final UserRepository userRepository;

    /**
     * Repository pour l'accès aux données des services.
     */
    private final ServiceRepository serviceRepository;

    /**
     * Mapper pour la conversion entre entités et DTOs de réservation.
     */
    private final ReservationMapper reservationMapper;

    /**
     * Récupère toutes les réservations de l'utilisateur authentifié.
     * L'utilisateur peut être soit client soit prestataire.
     * Vérifie que l'utilisateur existe en base et que le rôle correspond.
     * Le système détermine automatiquement le type d'utilisateur et retourne
     * les réservations appropriées selon le rôle.
     *
     * @return Liste des réservations de l'utilisateur selon son rôle
     * @throws UserNotFoundException si l'utilisateur n'existe pas en base
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'a pas le bon rôle
     */
    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        // Récupérer l'utilisateur authentifié
        Long userId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur existe en base
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        // Récupérer le rôle de l'utilisateur depuis la base de données
        UserRole databaseRole = currentUser.getUserRole();

        // Vérifier que l'utilisateur a le bon rôle pour accéder aux réservations
        if (databaseRole != UserRole.CLIENT && databaseRole != UserRole.PROVIDER) {
            throw new UnauthorizedReservationAccessException(
                    "Seuls les clients et prestataires peuvent accéder aux réservations");
        }

        // Récupérer les réservations selon le rôle de l'utilisateur
        List<Reservation> reservations;

        if (databaseRole == UserRole.CLIENT) {
            // Pour un client, récupérer toutes les réservations où il est le client
            reservations = reservationRepository.findByClientId(userId);
        } else if (databaseRole == UserRole.PROVIDER) {
            // Pour un prestataire, récupérer toutes les réservations où il est le
            // prestataire
            reservations = reservationRepository.findByProviderId(userId);
        } else {
            throw new UnauthorizedReservationAccessException("Rôle utilisateur non reconnu");
        }

        return reservations.stream()
                .map(reservationMapper::toResponseDTO)
                .toList();
    }

    /**
     * Récupère une réservation spécifique par son identifiant.
     * Vérifie que l'utilisateur authentifié a accès à cette réservation (client ou prestataire).
     * Vérifie que l'utilisateur existe en base et récupère son rôle
     * automatiquement.
     * Le système vérifie l'autorisation en comparant l'utilisateur avec les participants.
     *
     * @param id L'identifiant de la réservation
     * @return La réservation si trouvée et accessible
     * @throws ReservationNotFoundException Si la réservation n'existe pas ou si l'utilisateur n'y a pas accès
     * @throws UserNotFoundException si l'utilisateur n'existe pas en base
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'est pas autorisé
     */
    @Override
    public ReservationResponseDTO getReservationById(Long id) {
        // Récupérer l'utilisateur authentifié
        Long userId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur existe en base
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        // Récupérer le rôle de l'utilisateur depuis la base de données
        UserRole databaseRole = currentUser.getUserRole();

        // Récupérer la réservation
        Reservation reservation = reservationRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation introuvable ou non autorisée."));

        // Vérifier que l'utilisateur est présent dans la réservation selon son rôle
        boolean isClient = reservation.getClient().getId().equals(userId);
        boolean isProvider = reservation.getProvider().getId().equals(userId);

        if (databaseRole == UserRole.CLIENT && !isClient) {
            throw new UnauthorizedReservationAccessException(UNAUTHORIZED_MESSAGE);
        }

        if (databaseRole == UserRole.PROVIDER && !isProvider) {
            throw new UnauthorizedReservationAccessException(UNAUTHORIZED_MESSAGE);
        }

        if (!isClient && !isProvider) {
            throw new UnauthorizedReservationAccessException(UNAUTHORIZED_MESSAGE);
        }

        return reservationMapper.toResponseDTO(reservation);
    }

    /**
     * Permet à un prestataire de changer le statut d'une réservation.
     * Change le statut selon les transitions autorisées : PENDING → IN_PROGRESS →
     * CLOSED
     * Le rôle est automatiquement déterminé côté serveur.
     * Le système vérifie que l'utilisateur est bien le prestataire de la réservation.
     *
     * @param reservationId L'identifiant de la réservation à modifier
     * @return La réservation mise à jour avec le nouveau statut
     * @throws ReservationNotFoundException                Si la réservation n'existe pas
     * @throws InvalidReservationStatusTransitionException Si la réservation n'est pas dans un statut
     *                                                     permettant la transition
     * @throws UnauthorizedReservationAccessException      Si le prestataire n'est pas autorisé à modifier
     *                                                     cette réservation
     * @throws UserNotFoundException si l'utilisateur n'existe pas en base
     */
    @Override
    public ReservationResponseDTO changeStatusOfReservationByProvider(Long reservationId) {
        // Récupérer l'utilisateur authentifié
        Long providerId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur existe en base
        User currentUser = userRepository.findById(providerId)
                .orElseThrow(() -> new ReservationNotFoundException(USER_NOT_FOUND_MESSAGE));

        // Récupérer le rôle de l'utilisateur depuis la base de données
        UserRole databaseRole = currentUser.getUserRole();

        // Vérifier que l'utilisateur a le bon rôle pour accepter une réservation
        if (databaseRole != UserRole.PROVIDER) {
            throw new UnauthorizedReservationAccessException("Seuls les prestataires peuvent modifier le statut d'une réservation");
        }

        // Récupérer la réservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation introuvable"));

        // Vérifier que le prestataire est autorisé à accepter cette réservation
        if (!reservation.getProvider().getId().equals(providerId)) {
            throw new UnauthorizedReservationAccessException("Vous n'êtes pas autorisé à accepter cette réservation");
        }

        // Gérer les transitions de statut selon le statut actuel
        ReservationStatus newStatus;
        if (reservation.getStatus() == ReservationStatus.PENDING) {
            newStatus = ReservationStatus.IN_PROGRESS;
        } else if (reservation.getStatus() == ReservationStatus.IN_PROGRESS) {
            newStatus = ReservationStatus.CLOSED;
        } else {
            throw new InvalidReservationStatusTransitionException(
                    "Le statut actuel ne permet pas de transition");
        }

        // Changer le statut et sauvegarder
        reservation.setStatus(newStatus);
        Reservation updated = reservationRepository.save(reservation);

        return reservationMapper.toResponseDTO(updated);
    }

    /**
     * Crée une nouvelle réservation.
     * L'utilisateur authentifié doit être le client de la réservation.
     * La réservation est créée avec le statut PENDING.
     * Le système vérifie automatiquement l'autorisation et valide les données.
     *
     * @param dto Les données de création de la réservation
     * @return La réservation créée
     * @throws UnauthorizedReservationAccessException Si l'utilisateur n'est pas autorisé à créer cette
     *                                                réservation
     * @throws MissingReservationDataException si des données requises sont manquantes
     * @throws ServiceNotFoundException si le service spécifié n'existe pas
     */
    @Override
    public ReservationResponseDTO createReservation(ReservationDTO dto) {
        // Récupérer l'utilisateur authentifié
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // Vérifier que l'utilisateur authentifié est bien le client de la réservation
        if (!currentUserId.equals(dto.getClientId())) {
            throw new UnauthorizedReservationAccessException("Vous n'êtes pas autorisé à créer cette réservation");
        }

        // Récupérer le client
        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new MissingReservationDataException("Client introuvable"));

        // Récupérer le prestataire
        User provider = userRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new MissingReservationDataException("Prestataire introuvable"));

        // Récupérer le service
        Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ServiceNotFoundException("Service introuvable"));

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setProvider(provider);
        reservation.setService(service);
        reservation.setStartDate(dto.getStartDate().toLocalTime());
        reservation.setEndDate(dto.getEndDate().toLocalTime());
        reservation.setTotalPrice(dto.getTotalPrice());
        reservation.setStatus(ReservationStatus.PENDING);

        // Sauvegarder la réservation
        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponseDTO(savedReservation);
    }
}
