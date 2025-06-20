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

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getSiretSiren() {
        return siretSiren;
    }

    public void setSiretSiren(String siretSiren) {
        this.siretSiren = siretSiren;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Service> getPrestations() {
        return prestations;
    }

    public void setPrestations(List<Service> prestations) {
        this.prestations = prestations;
    }

    public List<Schedule> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Schedule> agendas) {
        this.agendas = agendas;
    }

    public List<Payment> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Payment> paiements) {
        this.paiements = paiements;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
