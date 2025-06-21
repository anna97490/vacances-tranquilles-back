package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

//    @ManyToOne
//    @JoinColumn(name = "user1_id")
//    private User user1;
//
//    @ManyToOne
//    @JoinColumn(name = "user2_id")
//    private User user2;
//
//    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
//    private List<Message> messages;
}