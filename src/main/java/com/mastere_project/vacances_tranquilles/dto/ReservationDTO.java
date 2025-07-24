package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private ReservationStatus status; 
    private LocalDateTime reservationDate;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;

    private SimpleUserDTO client;
    private SimpleUserDTO provider;
    private SimpleServiceDTO service;
    private PaymentDTO payment;
}
