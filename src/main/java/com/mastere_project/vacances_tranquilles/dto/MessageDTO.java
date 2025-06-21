package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private Long conversationId;
    private Long providerId;
}

