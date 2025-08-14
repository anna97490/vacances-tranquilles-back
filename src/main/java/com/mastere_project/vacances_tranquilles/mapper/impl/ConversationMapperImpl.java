package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.entity.Conversation;
import com.mastere_project.vacances_tranquilles.mapper.ConversationMapper;
import org.springframework.stereotype.Component;

/**
 * Implémentation du mapper pour la conversion entre Conversation et ConversationDTO.
 */
@Component
public class ConversationMapperImpl implements ConversationMapper {

    /**
     * Convertit une entité Conversation en DTO ConversationDTO.
     *
     * @param conversation l'entité Conversation
     * @return le DTO ConversationDTO
     */
    @Override
    public ConversationDTO toDto(Conversation conversation) {
        if (conversation == null) return null;

        ConversationDTO dto = new ConversationDTO();
        dto.setId(conversation.getId());
        dto.setUser1Id(conversation.getUser1() != null ? conversation.getUser1().getId() : null);
        dto.setUser2Id(conversation.getUser2() != null ? conversation.getUser2().getId() : null);
        dto.setCreatedAt(conversation.getCreatedAt());
        
        return dto;
    }

    /**
     * Convertit un DTO ConversationDTO en entité Conversation.
     * Les utilisateurs doivent être récupérés et définis dans le service.
     *
     * @param dto le DTO ConversationDTO
     * @return l'entité Conversation
     */
    @Override
    public Conversation toEntity(ConversationDTO dto) {
        if (dto == null) return null;

        Conversation conversation = new Conversation();
        conversation.setId(dto.getId());
        conversation.setCreatedAt(dto.getCreatedAt());
        
        return conversation;
    }
}
