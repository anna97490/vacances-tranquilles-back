package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des réservations.
 * Fournit des endpoints pour récupérer, filtrer et modifier les réservations.
 * 
 * @author Mastere Project Team
 * @version 1.0
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
     * L'utilisateur peut être soit client soit prestataire.
     *
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant la liste des réservations de l'utilisateur
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservationDTO>> getUserReservations(Principal principal) {

        Long userId = Long.parseLong(principal.getName());
        List<ReservationDTO> reservations = reservationService.getReservationsForUserId(userId);
        
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupère une réservation spécifique par son identifiant.
     * L'utilisateur doit être autorisé à accéder à cette réservation.
     *
     * @param id L'identifiant de la réservation à récupérer
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant la réservation ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id, Principal principal) {
        Long userId = Long.parseLong(principal.getName());

        try {
            ReservationDTO reservation = reservationService.getReservationByIdAndUserId(id, userId);
            
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère les réservations de l'utilisateur filtrées par statut.
     * Les statuts valides sont : PENDING, IN_PROGRESS, CLOSED.
     *
     * @param status Le statut de réservation pour filtrer les résultats
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant la liste des réservations filtrées
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStatus(@PathVariable String status,
            Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<ReservationDTO> reservations = reservationService.getReservationsByStatus(userId, status.toUpperCase());
        
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupère une réservation spécifique par son identifiant et son statut.
     * L'utilisateur doit être autorisé à accéder à cette réservation.
     *
     * @param id L'identifiant de la réservation à récupérer
     * @param status Le statut attendu de la réservation
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant la réservation ou 404 si non trouvée
     */
    @GetMapping("/{id}/status/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationDTO> getReservationByIdAndStatus(@PathVariable Long id,
            @PathVariable String status, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        try {
            ReservationDTO reservation = reservationService.getReservationByIdAndUserIdAndStatus(id, userId,
                    status.toUpperCase());
            
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Accepte une réservation en attente.
     * Seul le prestataire associé à la réservation peut l'accepter.
     * Le statut passe de PENDING à IN_PROGRESS.
     *
     * @param id L'identifiant de la réservation à accepter
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant la réservation mise à jour ou 404 si non trouvée
     */
    @PatchMapping("/{id}/accept")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationDTO> acceptReservation(@PathVariable Long id, Principal principal) {
        Long providerId = Long.parseLong(principal.getName());
        
        try {
            ReservationDTO updated = reservationService.acceptReservationByProvider(id, providerId);
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Finalise une réservation en cours.
     * Seul le prestataire associé à la réservation peut la finaliser.
     * Le statut passe de IN_PROGRESS à CLOSED.
     *
     * @param id L'identifiant de la réservation à finaliser
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant la réservation mise à jour ou 404 si non trouvée
     */
    @PatchMapping("/{id}/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationDTO> completeReservation(@PathVariable Long id, Principal principal) {
        Long providerId = Long.parseLong(principal.getName());
        
        try {
            ReservationDTO updated = reservationService.completeReservationByProvider(id, providerId);
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
