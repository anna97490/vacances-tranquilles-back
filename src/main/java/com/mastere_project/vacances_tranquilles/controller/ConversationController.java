package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.exception.ErrorEntity;
import com.mastere_project.vacances_tranquilles.service.ConversationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Contrôleur REST pour la gestion des conversations entre utilisateurs.
 */
@Data
class ConversationCreateRequest {
    private Long reservationId;
}

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {
    Logger log = Logger.getLogger(ConversationController.class.getName());

    private static final String ERROR_TYPE = "ERROR";

    private final ConversationService conversationService;

    /**
     * Récupère toutes les conversations de l'utilisateur connecté.
     *
     * @return une réponse contenant la liste des conversations de l'utilisateur
     */
    @GetMapping
    public ResponseEntity<List<ConversationSummaryDto>> getConversations() {
        List<ConversationSummaryDto> conversations = conversationService.getConversationsForUser();
        
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
            if (request == null || request.getReservationId() == null) {
                return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, "Reservation ID cannot be null"));
            }
            
            ConversationDTO conversation = conversationService.createConversation(request.getReservationId());
            
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, e.getMessage()));
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
            if (id == null) {
                return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, "Conversation ID cannot be null"));
            }
            
            ConversationDTO conversation = conversationService.getConversationById(id);
            
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, e.getMessage()));
        }
    }

    /**
     * Récupère la réservation associée à une conversation.
     *
     * @param id l'identifiant de la conversation
     * @return une réponse contenant la réservation associée, ou une erreur si non autorisé ou non trouvée
     */
    @GetMapping("/{id}/reservation")
    public ResponseEntity<Object> getReservationByConversationId(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, "Conversation ID cannot be null"));
            }
            
            ReservationResponseDTO reservation = conversationService.getReservationByConversationId(id);
            
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, e.getMessage()));
        }
    }
}
