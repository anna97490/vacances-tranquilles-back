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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime sentAt;

    // @ManyToOne
    // @JoinColumn(name = "conversation_id")
    // private Conversation conversation;
    //
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;
}