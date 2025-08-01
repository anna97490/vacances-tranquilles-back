package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un message échangé dans une conversation.
 */
@Data
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private Long conversationId;
    private Long providerId;
}
