package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un créneau de disponibilité d'un prestataire.
 */
@Data
@NoArgsConstructor
public class ScheduleDTO {

    public class ScheduleDto {
        private Long id;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Long providerId;
    }

    /**
     * DTO interne représentant un créneau horaire précis.
     */
}
