package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    
    private Long conversationId;
    
    private Long senderId;
    
    private String content;
    
    private java.time.LocalDateTime sentAt;
    
    private boolean read;
}

