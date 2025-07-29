package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StripeCheckoutSessionRequestDTO {
    private Long serviceId;
    private Long customerId;
    private Long providerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

}
