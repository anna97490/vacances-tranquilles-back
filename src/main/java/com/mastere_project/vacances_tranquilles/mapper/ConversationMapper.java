package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;

/**
 * Mapper pour convertir entre Conversation et ConversationDTO.
 */
public interface ConversationMapper {

    /**
     * Convertit une entité Conversation en DTO.
     * @param conversation l'entité Conversation
     * @return le DTO ConversationDTO
     */
    ConversationDTO toDto(Conversation conversation);
    
    
    /**
     * Convertit un DTO ConversationDTO en entité Conversation.
     * @param dto le DTO ConversationDTO
     * @return l'entité Conversation
     */
    Conversation toEntity(ConversationDTO dto);
} 