package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant une conversation entre deux utilisateurs.
 *
 * Contient les identifiants des participants, la date de création et la liste des messages associés.
 */
@Data
@NoArgsConstructor
public class ConversationDTO {

    private Long id;
    private Long user1Id;
    private Long user2Id;
    private LocalDateTime createdAt;
}
