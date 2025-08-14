package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;
import com.mastere_project.vacances_tranquilles.exception.ErrorEntity;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Contrôleur REST pour la gestion des messages dans les conversations.
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private static final String ERROR_TYPE = "ERROR";
    private final MessageService messageService;
    private final Logger log = Logger.getLogger(MessageController.class.getName());

    /**
     * Envoie un nouveau message dans une conversation.
     *
     * @param messageDTO le message à envoyer
     * @return une réponse contenant le message sauvegardé
     */
    @PostMapping
    public ResponseEntity<Object> sendMessage(@RequestBody MessageDTO messageDTO) {
        try {
            MessageDTO saved = messageService.sendMessage(messageDTO);
            
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, e.getMessage()));
        }
    }

    /**
     * Récupère tous les messages d'une conversation et marque comme lus ceux envoyés par l'autre utilisateur.
     *
     * @param conversationId l'identifiant de la conversation dont on veut les messages
     * @return une réponse contenant la liste des messages de la conversation
     */
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<Object> getMessagesByConversation(@PathVariable Long conversationId) {
        try {
            List<MessageResponseDTO> messages = messageService.getMessagesByConversationId(conversationId);
            
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, e.getMessage()));
        }
    }

    /**
     * Modifie un message existant (seul l'auteur peut modifier).
     *
     * @param id         l'identifiant du message à modifier
     * @param messageDTO le contenu modifié du message
     * @return une réponse contenant le message modifié ou une erreur si non autorisé ou non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMessage(@PathVariable Long id, @RequestBody MessageDTO messageDTO) {
        try {
            MessageDTO updated = messageService.updateMessage(id, messageDTO);
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.severe("Error updating message: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorEntity(ERROR_TYPE, e.getMessage()));
        }
    }
}