package com.mastere_project.vacances_tranquilles.entity;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Champs pour la conformité RGPD
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_anonymized")
    private Boolean isAnonymized = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Schedule> schedules;
}
