package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversationDTO {

    private Long id;
    private LocalDateTime createdAt;
    private Long user1Id;
    private Long user2Id;
}

