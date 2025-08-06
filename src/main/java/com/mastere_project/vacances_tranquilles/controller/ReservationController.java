package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.UpdateReservationStatusDTO;
import com.mastere_project.vacances_tranquilles.exception.MissingReservationDataException;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.ServiceNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UnauthorizedReservationAccessException;
import com.mastere_project.vacances_tranquilles.exception.InvalidReservationStatusTransitionException;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des réservations.
 * Fournit des endpoints pour récupérer, créer et modifier les réservations.
 * L'authentification et l'autorisation sont gérées automatiquement côté serveur.
 * 
 * @author VacancesTranquilles
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    /**
     * Service de gestion des réservations.
     */
    private final ReservationService reservationService;

    /**
     * Récupère toutes les réservations de l'utilisateur authentifié.
     * L'utilisateur peut être soit client soit prestataire selon son rôle.
     * Le système détermine automatiquement le type d'utilisateur et retourne
     * les réservations appropriées (réservations du client ou réservations du prestataire).
     *
     * @return ResponseEntity contenant la liste des réservations de l'utilisateur
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'est pas autorisé
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationService.getAllReservations();
        
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupère une réservation spécifique par son identifiant.
     * L'utilisateur doit être autorisé à accéder à cette réservation
     * (soit le client, soit le prestataire associé).
     *
     * @param id L'identifiant de la réservation à récupérer
     * @return ResponseEntity contenant la réservation ou 404 si non trouvée
     * @throws ReservationNotFoundException si la réservation n'existe pas
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'est pas autorisé
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        ReservationResponseDTO reservation = reservationService.getReservationById(id);
        
        return ResponseEntity.ok(reservation);
    }

    /**
     * Crée une nouvelle réservation.
     * L'utilisateur authentifié doit être le client de la réservation.
     * Le système vérifie automatiquement l'autorisation et valide les données.
     *
     * @param reservationDTO Les données de création de la réservation
     * @return ResponseEntity contenant la réservation créée
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'est pas autorisé
     * @throws MissingReservationDataException si des données requises sont manquantes
     * @throws ServiceNotFoundException si le service spécifié n'existe pas
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationResponseDTO created = reservationService.createReservation(reservationDTO);
        
        return ResponseEntity.ok(created);
    }

    /**
     * Modifie le statut d'une réservation.
     * Seul le prestataire associé à la réservation peut modifier le statut.
     * Les transitions possibles : PENDING → IN_PROGRESS ou CANCELLED, IN_PROGRESS → CLOSED.
     * Le système vérifie automatiquement l'autorisation et la validité de la transition.
     *
     * @param id L'identifiant de la réservation à modifier
     * @param dto DTO contenant le nouveau statut de la réservation (status requis)
     * @return ResponseEntity contenant la réservation mise à jour
     * @throws ReservationNotFoundException si la réservation n'existe pas
     * @throws UnauthorizedReservationAccessException si l'utilisateur n'est pas autorisé
     * @throws InvalidReservationStatusTransitionException si la transition de statut est invalide
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationResponseDTO> updateStatus(@PathVariable Long id, @RequestBody UpdateReservationStatusDTO dto) {
        ReservationResponseDTO updated = reservationService.changeStatusOfReservationByProvider(id, dto);
    return ResponseEntity.ok(updated);
}
}
