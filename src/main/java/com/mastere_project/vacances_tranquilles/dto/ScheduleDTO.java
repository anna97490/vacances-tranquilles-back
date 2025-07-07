package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un créneau d'indisponibilité d'un prestataire.
 */
@Data
@NoArgsConstructor
public class ScheduleDTO {

    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long providerId;

}
