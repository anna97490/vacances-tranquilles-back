package com.mastere_project.vacances_tranquilles.model;

import com.mastere_project.vacances_tranquilles.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profilePicture;
    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ProviderDetails providerDetails;
}
