package com.mastere_project.vacances_tranquilles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour une conversation avec ses messages associés.
 * Contient la conversation et la liste de ses messages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationWithMessagesDTO {

    private ConversationDTO conversation;
    private List<MessageDTO> messages;
} 