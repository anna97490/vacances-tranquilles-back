package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la représentation d'une conversation.
 */
@Data
@NoArgsConstructor
public class ConversationDTO {

    private Long id;

    private Long user1Id;

    private Long user2Id;

    private LocalDateTime createdAt;

    private List<Long> messageIds;
}

