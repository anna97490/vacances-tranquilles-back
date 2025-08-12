package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.UpdateReservationStatusDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Service;
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
 * Cette implémentation gère l'authentification et l'autorisation
 * automatiquement.
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final String USER_NOT_FOUND_MESSAGE = "Utilisateur introuvable";
    private static final String UNAUTHORIZED_MESSAGE = "Vous n'êtes pas autorisé à accéder à cette réservation";

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        Long userId = SecurityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        
        UserRole databaseRole = currentUser.getUserRole();
        
        if (databaseRole != UserRole.CLIENT && databaseRole != UserRole.PROVIDER) {
            throw new UnauthorizedReservationAccessException(
                    "Seuls les clients et prestataires peuvent accéder aux réservations");
        }

        List<Reservation> reservations;
        if (databaseRole == UserRole.CLIENT) {
            reservations = reservationRepository.findByClientId(userId);
        } else {
            reservations = reservationRepository.findByProviderId(userId);
        }

        return reservations.stream()
                .map(reservationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public ReservationResponseDTO getReservationById(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        
        UserRole databaseRole = currentUser.getUserRole();
        
        Reservation reservation = reservationRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation introuvable ou non autorisée."));

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

    @Override
    public ReservationResponseDTO changeStatusOfReservationByProvider(Long reservationId, UpdateReservationStatusDTO dto) {
        Long providerId = SecurityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(providerId)
                .orElseThrow(() -> new ReservationNotFoundException(USER_NOT_FOUND_MESSAGE));
        
        UserRole databaseRole = currentUser.getUserRole();
        
        if (databaseRole != UserRole.PROVIDER) {
            throw new UnauthorizedReservationAccessException(
                    "Seuls les prestataires peuvent modifier le statut d'une réservation");
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Réservation introuvable"));

        if (!reservation.getProvider().getId().equals(providerId)) {
            throw new UnauthorizedReservationAccessException("Vous n'êtes pas autorisé à accepter cette réservation");
        }

        ReservationStatus current = reservation.getStatus();
        ReservationStatus next = dto.getStatus();

        if (current == ReservationStatus.PENDING
                && (next == ReservationStatus.IN_PROGRESS || next == ReservationStatus.CANCELLED)) {
            reservation.setStatus(next);
        } else if (current == ReservationStatus.IN_PROGRESS && next == ReservationStatus.CLOSED) {
            reservation.setStatus(next);
        } else {
            throw new IllegalArgumentException("Transition de statut invalide : de " + current + " vers " + next);
        }

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toResponseDTO(saved);
    }

    @Override
    public ReservationResponseDTO createReservation(ReservationDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        if (!currentUserId.equals(dto.getClientId())) {
            throw new UnauthorizedReservationAccessException("Vous n'êtes pas autorisé à créer cette réservation");
        }

        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new MissingReservationDataException("Client introuvable"));

        User provider = userRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new MissingReservationDataException("Prestataire introuvable"));

        Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ServiceNotFoundException("Service introuvable"));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setProvider(provider);
        reservation.setService(service);
        reservation.setReservationDate(dto.getReservationDate().toLocalDate());
        reservation.setStartDate(dto.getStartDate().toLocalTime());
        reservation.setEndDate(dto.getEndDate().toLocalTime());
        reservation.setTotalPrice(dto.getTotalPrice());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toResponseDTO(savedReservation);
    }
}