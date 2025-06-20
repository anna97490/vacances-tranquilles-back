package com.mastere_project.vacances_tranquilles.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduleDTO {
        
    private Long id;
    private Date date;
    private boolean isBooked;

    private Long serviceId;        // ID de la prestation associée
    private String serviceTitle;   // Titre de la prestation (optionnel)

    private Long userId;           // ID du particulier qui a réservé (nullable si pas encore réservé)
    private String userFullName;   // Nom complet du particulier (optionnel)
}
