package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une conversation entre deux utilisateurs (client ou prestataire).
 */
@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    /** L'identifiant unique de la conversation. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Premier participant à la conversation (client ou prestataire). */
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    /** Second participant à la conversation (client ou prestataire). */
    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    /** Liste des messages échangés dans la conversation. */
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    /** Date et heure de création de la conversation. */
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}