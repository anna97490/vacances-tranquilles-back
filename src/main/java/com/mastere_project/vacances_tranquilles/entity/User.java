package com.mastere_project.vacances_tranquilles.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
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
    private String role; // PARTICULIER, PRESTATAIRE, ADMIN

    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;

    // Champs uniquement pertinents pour les prestataires
    private String siretSiren;
    private String companyName;

    // Relations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Service> prestations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Schedule> agendas;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> paiements;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Conversation> conversations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages;
}
