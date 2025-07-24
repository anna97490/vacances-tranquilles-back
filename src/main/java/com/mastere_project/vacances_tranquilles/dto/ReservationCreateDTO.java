package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationCreateDTO {
    private Long clientId;
    private Long providerId;
    private Long serviceId;
    private Long paymentId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String comment;
    private Double totalPrice;
}
