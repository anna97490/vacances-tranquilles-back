package com.mastere_project.vacances_tranquilles.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceDTO {

    private Long id;
    private String invoiceNumber;
    private Date issueDate;
    private Double amount;
    private String paymentMethod;

    private Long providerId;
    private String providerFullName;

    private Long paymentId;
    private Date paymentDate;

    private Long scheduleId;
    private Date scheduleDate;

    private Long serviceId;
    private String serviceTitle;
}

