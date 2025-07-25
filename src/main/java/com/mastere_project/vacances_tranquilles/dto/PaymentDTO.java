package com.mastere_project.vacances_tranquilles.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un paiement effectué par un utilisateur.
 */
@Data
@NoArgsConstructor
public class PaymentDTO {

    private Long id;
    private Double amount;
    private Date paymentDate;
    private String method;

    private Long userId;
    private String userFullName;

    private Long scheduleId;
}
