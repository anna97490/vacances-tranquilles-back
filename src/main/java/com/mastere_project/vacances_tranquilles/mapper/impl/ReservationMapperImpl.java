package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.mapper.ReservationMapper;
import org.springframework.stereotype.Component;

/**
 * Implémentation du mapper pour la conversion entre entités Reservation et
 * DTOs.
 * Fournit la logique de transformation des objets Reservation vers les
 * différents types de DTOs.
 * Cette implémentation gère la conversion des dates et des champs de base de
 * manière sécurisée.
 */
@Component
public class ReservationMapperImpl implements ReservationMapper {

    /**
     * Convertit une entité Reservation en ReservationDTO.
     * Mappe tous les champs de base nécessaires pour les opérations de création et modification.
     * Effectue la conversion des dates en LocalDateTime pour la compatibilité avec les DTOs.
     * Gère les cas où l'entité reservation est null.
     *
     * @param reservation L'entité Reservation à convertir
     * @return Le ReservationDTO correspondant, ou null si reservation est null
     * @throws IllegalArgumentException si les données de réservation sont invalides
     */
    @Override
    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setStatus(reservation.getStatus());
        
        // Gestion sécurisée des dates avec vérification null
        if (reservation.getReservationDate() != null) {
            dto.setReservationDate(reservation.getReservationDate().atStartOfDay());
            if (reservation.getStartDate() != null) {
                dto.setStartDate(reservation.getReservationDate().atTime(reservation.getStartDate()));
            }
            if (reservation.getEndDate() != null) {
                dto.setEndDate(reservation.getReservationDate().atTime(reservation.getEndDate()));
            }
        }
        
        dto.setTotalPrice(reservation.getTotalPrice());

        return dto;
    }

    /**
     * Convertit une entité Reservation en ReservationResponseDTO.
     * Mappe uniquement les champs de base pour les réponses sécurisées vers le frontend.
     * Exclut les informations sensibles des objets associés (client, prestataire, service, paiement).
     * Effectue la conversion des dates en LocalDateTime pour la compatibilité.
     *
     * @param reservation L'entité Reservation à convertir
     * @return Le ReservationResponseDTO correspondant, ou null si reservation est null
     * @throws IllegalArgumentException si les données de réservation sont invalides
     */
    @Override
    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setStatus(reservation.getStatus());
        
        // Gestion sécurisée des dates avec vérification null
        if (reservation.getReservationDate() != null) {
            dto.setReservationDate(reservation.getReservationDate().atStartOfDay());
            if (reservation.getStartDate() != null) {
                dto.setStartDate(reservation.getReservationDate().atTime(reservation.getStartDate()));
            }
            if (reservation.getEndDate() != null) {
                dto.setEndDate(reservation.getReservationDate().atTime(reservation.getEndDate()));
            }
        }
        
        dto.setTotalPrice(reservation.getTotalPrice());
        
        // Mapper les IDs des utilisateurs
        if (reservation.getClient() != null) {
            dto.setClientId(reservation.getClient().getId());
            dto.setClientName(reservation.getClient().getFirstName() + " " + reservation.getClient().getLastName());
            dto.setClientEmail(reservation.getClient().getEmail());
        }
        
        if (reservation.getProvider() != null) {
            dto.setProviderId(reservation.getProvider().getId());
            dto.setProviderName(reservation.getProvider().getFirstName() + " " + reservation.getProvider().getLastName());
            dto.setProviderEmail(reservation.getProvider().getEmail());
        }
        
        // Mapper l'ID de la conversation si elle existe
        if (reservation.getConversation() != null) {
            dto.setConversationId(reservation.getConversation().getId());
        }

        return dto;
    }
}
