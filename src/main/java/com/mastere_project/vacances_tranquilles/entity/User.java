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

    /** L'identifiant unique de l'utilisateur. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** L'URL de la photo de profil de l'utilisateur. */
    private String profilePicture;

    /** Le prénom de l'utilisateur. */
    @Column(nullable = false)
    private String firstName;

    /** Le nom de famille de l'utilisateur. */
    @Column(nullable = false)
    private String lastName;

    /** L'adresse email de l'utilisateur (unique). */
    @Column(nullable = false, unique = true)
    private String email;

    /** Le mot de passe de l'utilisateur (crypté). */
    @Column(nullable = false)
    private String password;

    /** Le rôle de l'utilisateur (particulier, prestataire, admin). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    /** Le numéro de téléphone de l'utilisateur. */
    @Column(nullable = false)
    private String phoneNumber;

    /** L'adresse postale de l'utilisateur. */
    @Column(nullable = false)
    private String address;

    /** La ville de résidence de l'utilisateur. */
    @Column(nullable = false)
    private String city;

    /** Le code postal de la ville de l'utilisateur. */
    @Column(nullable = false)
    private String postalCode;

    /** Le numéro SIRET ou SIREN de l'entreprise (pour les prestataires). */
    private String siretSiren;
    /** Le nom de la société de l'utilisateur (pour les prestataires). */
    private String companyName;

    /** Liste des conversations où l'utilisateur est user1. */
    @OneToMany(mappedBy = "user1")
    private List<Conversation> conversationsAsUser1 = new ArrayList<>();

    /** Liste des conversations où l'utilisateur est user2. */
    @OneToMany(mappedBy = "user2")
    private List<Conversation> conversationsAsUser2 = new ArrayList<>();

    /**
     * Retourne la liste de toutes les conversations de l'utilisateur (en tant que user1 ou user2).
     * @return liste de toutes les conversations
     */
    @Transient
    public List<Conversation> getAllConversations() {
        List<Conversation> all = new ArrayList<>();
        all.addAll(conversationsAsUser1);
        all.addAll(conversationsAsUser2);
        return all;
    }

    /** Liste des messages envoyés par l'utilisateur. */
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
}
