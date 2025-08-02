package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des messages dans les conversations.
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * Envoie un nouveau message dans une conversation.
     *
     * @param messageDTO le message à envoyer
     * @return une réponse contenant le message sauvegardé
     */
    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        MessageDTO saved = messageService.sendMessage(messageDTO);
        
        return ResponseEntity.ok(saved);
    }

    /**
     * Récupère tous les messages d'une conversation et marque comme lus ceux envoyés par l'autre utilisateur.
     *
     * @param conversationId l'identifiant de la conversation dont on veut les messages
     * @return une réponse contenant la liste des messages de la conversation
     */
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByConversation(@PathVariable Long conversationId) {
        List<MessageDTO> messages = messageService.getMessagesByConversationId(conversationId);
        
        return ResponseEntity.ok(messages);
    }

    /**
     * Modifie un message existant (seul l'auteur peut modifier).
     *
     * @param id         l'identifiant du message à modifier
     * @param messageDTO le contenu modifié du message
     * @return une réponse contenant le message modifié ou une erreur si non autorisé ou non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> updateMessage(@PathVariable Long id, @RequestBody MessageDTO messageDTO) {
        try {
            MessageDTO updated = messageService.updateMessage(id, messageDTO);
            return ResponseEntity.ok(updated);
        } catch (com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException e) {
            return ResponseEntity.status(403).build();
        }
    }
}