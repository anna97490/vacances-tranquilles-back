package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.entity.Message;

/**
 * Mapper pour convertir entre Message et MessageDTO.
 */
public interface MessageMapper {

    /**
     * Convertit une entité Message en DTO.
     * @param message l'entité Message
     * @return le DTO MessageDTO
     */
    MessageDTO toDto(Message message);
    
    
    /**
     * Convertit un DTO MessageDTO en entité Message.
     * @param dto le DTO MessageDTO
     * @return l'entité Message
     */
    Message toEntity(MessageDTO dto);
} 