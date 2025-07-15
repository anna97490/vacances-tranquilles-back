package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Représente un message envoyé dans une conversation.
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean read = false;
}