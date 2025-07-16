package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un message échangé dans une conversation.
 */
@Data
@NoArgsConstructor
public class MessageDTO {

    /** L'identifiant unique du message. */
    private Long id;
    
    /** L'identifiant de la conversation à laquelle appartient le message. */
    private Long conversationId;
    
    /** L'identifiant de l'expéditeur du message. */
    private Long senderId;
    
    /** Le contenu textuel du message. */
    private String content;
    
    /** La date et l'heure d'envoi du message. */
    private java.time.LocalDateTime sentAt;
    
    /** Indique si le message a été lu par le destinataire. */
    private boolean read;
}
