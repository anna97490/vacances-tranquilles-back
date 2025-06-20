package com.mastere_project.vacances_tranquilles.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    private String content;
    private Date sentAt;

    private Long conversationId;

    private Long userId;
    private String userFullName;
}

