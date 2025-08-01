package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant une conversation entre deux utilisateurs.
 */
@Data
@NoArgsConstructor
public class ConversationDTO {

    private Long id;
    private LocalDateTime createdAt;
    private Long user1Id;
    private Long user2Id;
}
