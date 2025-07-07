package com.mastere_project.vacances_tranquilles.entity;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant un utilisateur (client ou prestataire) de la
 * plateforme.
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

    // @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    // private List<Reservation> reservationAsCustomer;
    //
    // @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    // private List<Reservation> reservationAsProvider;
    //
    // @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    // private List<Schedule> schedules;
    //
    // @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    // private List<Conversation> conversationsInitiated;
    //
    // @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    // private List<Conversation> conversationsReceived;
    //
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private List<Message> messages;
}
