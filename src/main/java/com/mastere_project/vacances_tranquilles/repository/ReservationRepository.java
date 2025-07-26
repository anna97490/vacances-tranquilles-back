package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Repository pour la gestion des entités Reservation.
 * Fournit les opérations CRUD de base pour les réservations.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // JpaRepository fournit déjà findById(Long id)
} 