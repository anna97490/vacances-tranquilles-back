package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_shouldReturnSavedMessage() {
        MessageDTO input = new MessageDTO();
        input.setContent("Hello");
        input.setConversationId(1L);
        MessageDTO saved = new MessageDTO();
        saved.setId(1L);
        saved.setContent("Hello");
        saved.setConversationId(1L);
        when(messageService.sendMessage(any(MessageDTO.class))).thenReturn(saved);

        ResponseEntity<MessageDTO> response = messageController.sendMessage(input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    void getMessagesByConversation_shouldReturnMessages() {
        MessageDTO msg1 = new MessageDTO();
        msg1.setId(1L);
        MessageDTO msg2 = new MessageDTO();
        msg2.setId(2L);
        List<MessageDTO> messages = Arrays.asList(msg1, msg2);
        when(messageService.getMessagesByConversationId(5L)).thenReturn(messages);

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesByConversation(5L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void updateMessage_shouldReturnUpdatedMessage() {
        MessageDTO input = new MessageDTO();
        input.setContent("Updated");
        MessageDTO updated = new MessageDTO();
        updated.setId(1L);
        updated.setContent("Updated");
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class))).thenReturn(updated);

        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void updateMessage_shouldReturnNotFound_whenNotFound() {
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class)))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException("not found"));
        MessageDTO input = new MessageDTO();
        
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnForbidden_whenForbidden() {
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class)))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException("forbidden"));
        MessageDTO input = new MessageDTO();
        
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void sendMessage_shouldReturnBadRequest_whenMessageNull() {
        ResponseEntity<MessageDTO> response = messageController.sendMessage(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendMessage_shouldReturnBadRequest_whenConversationIdNull() {
        MessageDTO message = new MessageDTO();
        message.setConversationId(null);
        message.setContent("test");
        
        ResponseEntity<MessageDTO> response = messageController.sendMessage(message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnBadRequest_whenIdNull() {
        MessageDTO message = new MessageDTO();
        message.setContent("updated");
        
        ResponseEntity<MessageDTO> response = messageController.updateMessage(null, message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnBadRequest_whenMessageNull() {
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getMessagesByConversation_shouldReturnBadRequest_whenIdNull() {
        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesByConversation(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getMessagesByConversation_shouldReturnEmptyList() {
        when(messageService.getMessagesByConversationId(1L)).thenReturn(List.of());

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesByConversation(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
} 