package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant une conversation entre deux utilisateurs.
 *
 * Contient les identifiants des participants, la date de création et la liste des messages associés.
 */
@Data
@NoArgsConstructor
public class ConversationDTO {

    /** L'identifiant unique de la conversation. */
    private Long id;

    /** L'identifiant du premier utilisateur participant à la conversation. */
    private Long user1Id;

    /** L'identifiant du second utilisateur participant à la conversation. */
    private Long user2Id;

    /** La date et l'heure de création de la conversation. */
    private LocalDateTime createdAt;

    /** La liste des identifiants des messages associés à la conversation. */
    private List<Long> messageIds;
}
