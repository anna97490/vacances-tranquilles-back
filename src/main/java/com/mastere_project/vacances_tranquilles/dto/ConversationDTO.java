package com.mastere_project.vacances_tranquilles.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversationDTO {

    private Long id;
    private Date createdAt;

    private Long userId;
    private String userFullName;

    private List<MessageDTO> messages; 
}

