package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;

/**
 * Mapper pour convertir entre Conversation et ConversationDTO.
 */
public interface ConversationMapper {

    /**
     * Convertit une entité Conversation en ConversationDTO.
     *
     * @param conversation l'entité Conversation à convertir
     * @return le DTO ConversationDTO correspondant
     */
    ConversationDTO toDto(Conversation conversation);
    
    
    /**
     * Convertit un DTO ConversationDTO en entité Conversation.
     *
     * @param dto le DTO ConversationDTO à convertir
     * @return l'entité Conversation correspondante
     */
    Conversation toEntity(ConversationDTO dto);
} 