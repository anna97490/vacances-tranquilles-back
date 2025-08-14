package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un message dans une conversation avec les informations d'affichage.
 */
@Data
@NoArgsConstructor
public class MessageResponseDTO {
    
    private Long id;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private String myName;

    public MessageResponseDTO(Long id, String senderName, String content, LocalDateTime sentAt, Boolean isRead, String myName) {
        this.id = id;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = isRead;
        this.myName = myName;
    }
}