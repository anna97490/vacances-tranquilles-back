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

    /** La conversation concernée. */
    private ConversationDTO conversation;
    
    /** La liste des messages associés à la conversation. */
    private List<MessageDTO> messages;
} 