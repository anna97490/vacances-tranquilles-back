package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationWithMessagesDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.exception.ErrorEntity;
import com.mastere_project.vacances_tranquilles.service.ConversationService;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des conversations entre utilisateurs.
 */
@Data
class ConversationCreateRequest {
    private Long otherUserId;
    private Long reservationId;
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
     * @return une réponse contenant la liste des conversations de l'utilisateur
     */
    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getConversations() {
        List<ConversationDTO> conversations = conversationService.getConversationsForUser();
        
        return ResponseEntity.ok(conversations);
    }

    /**
     * Crée une nouvelle conversation avec un autre utilisateur.
     *
     * @param request les informations de création de la conversation (identifiant de l'autre utilisateur, réservation)
     * @return une réponse contenant la conversation créée ou une erreur en cas d'échec
     */
    @PostMapping
    public ResponseEntity<Object> createConversation(@RequestBody ConversationCreateRequest request) {
        try {
            ConversationDTO conversation = conversationService.createConversation(request.getOtherUserId(), request.getReservationId());
            
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorEntity("ERROR", e.getMessage()));
        }
    }

    /**
     * Récupère une conversation par son ID.
     *
     * @param id l'identifiant de la conversation à récupérer
     * @return une réponse contenant la conversation, ou une erreur si non autorisé ou non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getConversationById(@PathVariable Long id) {
        try {
            ConversationDTO conversation = conversationService.getConversationById(id);
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorEntity("ERROR", e.getMessage()));
        }
    }

    /**
     * Récupère une conversation par son ID avec ses messages.
     * Marque comme lus les messages non lus envoyés par l'autre utilisateur.
     *
     * @param id l'identifiant de la conversation à récupérer
     * @return une réponse contenant la conversation et ses messages, ou une erreur si non autorisé ou non trouvée
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity<ConversationWithMessagesDTO> getConversationWithMessages(@PathVariable Long id) {
        try {
            ConversationDTO conversation = conversationService.getConversationById(id);
            List<MessageDTO> messages = messageService.getMessagesByConversationId(id);
            
            ConversationWithMessagesDTO result = new ConversationWithMessagesDTO(conversation, messages);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
