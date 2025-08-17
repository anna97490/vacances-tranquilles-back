package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReviewWithReviewerDTO {
    private Long id;
    private int note;
    private String commentaire;
    private Long reservationId;
    private Long reviewerId;
    private Long reviewedId;
    private LocalDateTime createdAt;
    
    // Informations du reviewer
    private String reviewerFirstName;
}
