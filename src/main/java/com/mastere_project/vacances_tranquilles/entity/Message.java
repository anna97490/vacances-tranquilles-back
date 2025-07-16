package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entité JPA représentant un message échangé dans une conversation.
 */
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /** L'identifiant unique du message. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** La conversation à laquelle appartient le message. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    /** L'utilisateur ayant envoyé le message. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    /** Le contenu textuel du message. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** La date et l'heure d'envoi du message. */
    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    /** Indique si le message a été lu par le destinataire. */
    @Column(nullable = false)
    private boolean read = false;
}