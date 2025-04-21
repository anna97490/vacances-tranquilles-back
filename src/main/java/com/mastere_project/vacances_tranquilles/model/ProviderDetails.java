package com.mastere_project.vacances_tranquilles.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String siretSiren;
    private String companyName;
    private String rcNumber;
    private String kbisUrl;
    private String autoEntrepreneurAttestationUrl;
    private String insuranceCertificateUrl;
}
