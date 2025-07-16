package com.mastere_project.vacances_tranquilles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un utilisateur (client ou prestataire) de la plateforme.
 */
@Data
@NoArgsConstructor
public class UserDTO {

    /** L'identifiant unique de l'utilisateur. */
    private Long id;
    
    /** Le prénom de l'utilisateur. */
    private String firstName;
    
    /** Le nom de famille de l'utilisateur. */
    private String lastName;
    
    /** L'URL de la photo de profil de l'utilisateur. */
    private String profilePicture;
    
    /** L'adresse email de l'utilisateur. */
    private String email;
    
    /** Le mot de passe de l'utilisateur (crypté). */
    private String password;
    
    /** Le rôle de l'utilisateur (client ou prestataire). */
    private com.mastere_project.vacances_tranquilles.model.enums.UserRole userRole;
    
    /** Le numéro de téléphone de l'utilisateur. */
    private String phoneNumber;
    
    /** L'adresse postale de l'utilisateur. */
    private String address;
    
    /** La ville de résidence de l'utilisateur. */
    private String city;
    
    /** Le code postal de la ville de l'utilisateur. */
    private String postalCode;
    
    /** Le numéro SIRET ou SIREN de l'entreprise (pour les prestataires). */
    private String siretSiren;
    
    /** Le nom de la société de l'utilisateur (pour les prestataires). */
    private String companyName;
    
    /** Liste des identifiants de conversations où l'utilisateur est user1. */
    private java.util.List<Long> conversationsAsUser1;
    
    /** Liste des identifiants de conversations où l'utilisateur est user2. */
    private java.util.List<Long> conversationsAsUser2;
    
    /** Liste des identifiants des messages envoyés par l'utilisateur. */
    private java.util.List<Long> messagesSentIds;
}
