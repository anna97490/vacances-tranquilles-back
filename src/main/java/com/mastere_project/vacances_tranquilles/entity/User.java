package com.mastere_project.vacances_tranquilles.entity;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur de la plateforme.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profilePicture;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole; // PARTICULIER, PRESTATAIRE, ADMIN

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    // Champs uniquement pertinents pour les prestataires
    private String siretSiren;
    private String companyName;

    // Relations avec les conversations
    @OneToMany(mappedBy = "user1")
    private List<Conversation> conversationsAsUser1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2")
    private List<Conversation> conversationsAsUser2 = new ArrayList<>();

    @Transient
    public List<Conversation> getAllConversations() {
        List<Conversation> all = new ArrayList<>();
        all.addAll(conversationsAsUser1);
        all.addAll(conversationsAsUser2);
        return all;
    }

    // Messages envoyés
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messagesSent = new ArrayList<>();


//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
//    private List<Reservation> reservationAsCustomer;
//
//    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
//    private List<Reservation> reservationAsProvider;
//
//    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
//    private List<Schedule> schedules;
//
}


