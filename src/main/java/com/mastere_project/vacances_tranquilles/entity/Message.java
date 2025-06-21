package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

//    @ManyToOne
//    @JoinColumn(name = "conversation_id")
//    private Conversation conversation;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
}