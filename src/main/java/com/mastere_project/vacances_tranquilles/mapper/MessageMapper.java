package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.entity.Message;

/**
 * Mapper pour convertir entre Message et MessageDTO.
 */
public interface MessageMapper {

    /**
     * Convertit une entité Message en MessageDTO.
     *
     * @param message l'entité Message à convertir
     * @return le DTO MessageDTO correspondant
     */
    MessageDTO toDto(Message message);
    
    
    /**
     * Convertit un DTO MessageDTO en entité Message.
     *
     * @param dto le DTO MessageDTO à convertir
     * @return l'entité Message correspondante
     */
    Message toEntity(MessageDTO dto);
} 