package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.entity.Message;
import com.mastere_project.vacances_tranquilles.mapper.MessageMapper;
import org.springframework.stereotype.Component;

/**
 * Implémentation du mapper pour la conversion entre Message et MessageDTO.
 */
@Component
public class MessageMapperImpl implements MessageMapper {

    /**
     * Convertit une entité Message en DTO MessageDTO.
     *
     * @param message l'entité Message
     * @return le DTO MessageDTO
     */
    @Override
    public MessageDTO toDto(Message message) {
        if (message == null) return null;

        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversation() != null ? message.getConversation().getId() : null);
        dto.setSenderId(message.getSender() != null ? message.getSender().getId() : null);
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        dto.setRead(message.isRead());
        return dto;
    }

    /**
     * Convertit un DTO MessageDTO en entité Message.
     * La conversation et l'utilisateur doivent être définis dans le service.
     *
     * @param dto le DTO MessageDTO
     * @return l'entité Message
     */
    @Override
    public Message toEntity(MessageDTO dto) {
        if (dto == null) return null;

        Message message = new Message();
        message.setId(dto.getId());
        message.setContent(dto.getContent());
        message.setSentAt(dto.getSentAt());
        message.setRead(dto.isRead());
        return message;
    }
}
