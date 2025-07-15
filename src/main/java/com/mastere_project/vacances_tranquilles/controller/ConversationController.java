package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationWithMessagesDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.service.ConversationService;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des conversations entre utilisateurs.
 */
@Data
class ConversationCreateRequest {
    private Long otherUserId;
    private String status;
}

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    /**
     * Récupère toutes les conversations de l'utilisateur connecté.
     * 
     * @param principal l'utilisateur authentifié
     * @return la liste des conversations
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConversationDTO>> getConversations(Principal principal) {

        // Récupération du user courant depuis le principal
        Long userId = Long.parseLong(principal.getName());
        List<ConversationDTO> conversations = conversationService.getConversationsForUser(userId);

        return ResponseEntity.ok(conversations);
    }


    /**
     * Crée une nouvelle conversation avec un autre utilisateur.
     * 
     * @param request   les informations de création de la conversation (autre utilisateur, statut)
     * @param principal l'utilisateur authentifié
     * @return la conversation créée ou une erreur
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConversationDTO> createConversation(
            @RequestBody ConversationCreateRequest request,
            Principal principal) {

        Long creatorId = Long.parseLong(principal.getName());

        // Contrôle métier : n'autoriser la création que si le statut est "in_progress"
        if (!"in_progress".equalsIgnoreCase(request.getStatus())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ConversationDTO conversation = conversationService.createConversation(
                    creatorId,
                    request.getOtherUserId());

            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * Récupère une conversation par son ID avec ses messages.
     * Marque comme lus les messages non lus envoyés par l'autre utilisateur.
     * 
     * @param id        l'identifiant de la conversation
     * @param principal l'utilisateur authentifié
     * @return la conversation et ses messages, ou une erreur si non autorisé
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConversationWithMessagesDTO> getConversationWithMessages(
            @PathVariable Long id,
            Principal principal) {

        Long userId = Long.parseLong(principal.getName());
        ConversationDTO conversation;
        
        try {
            conversation = conversationService.getConversationById(id, userId);
        } catch (com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException e) {
            return ResponseEntity.status(403).build();
        }

        List<MessageDTO> messages = messageService.getMessagesByConversationId(id, userId);
        ConversationWithMessagesDTO dto = new ConversationWithMessagesDTO(conversation, messages);

        return ResponseEntity.ok(dto);
    }
}
