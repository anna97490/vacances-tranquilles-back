package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        // Given
        MessageDTO input = new MessageDTO();
        input.setConversationId(1L);
        input.setContent("Hello");
        
        MessageDTO saved = new MessageDTO();
        saved.setId(1L);
        saved.setContent("Hello");
        when(messageService.sendMessage(input)).thenReturn(saved);

        // When
        ResponseEntity<MessageDTO> response = messageController.sendMessage(input);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    void sendMessage_shouldReturnBadRequest_whenMessageNull() {
        // When
        ResponseEntity<MessageDTO> response = messageController.sendMessage(null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendMessage_shouldReturnBadRequest_whenConversationIdNull() {
        // Given
        MessageDTO message = new MessageDTO();
        message.setConversationId(null);
        message.setContent("test");
        
        // When
        ResponseEntity<MessageDTO> response = messageController.sendMessage(message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendMessage_shouldReturnBadRequest_whenException() {
        // Given
        MessageDTO message = new MessageDTO();
        message.setConversationId(1L);
        message.setContent("test");
        when(messageService.sendMessage(message)).thenThrow(new RuntimeException("Error"));
        
        // When
        ResponseEntity<MessageDTO> response = messageController.sendMessage(message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getMessagesByConversation_shouldReturnMessages() {
        // Given
        MessageResponseDTO msg1 = new MessageResponseDTO("User1", "Hello", LocalDateTime.now(), true, "Me");
        MessageResponseDTO msg2 = new MessageResponseDTO("User2", "Hi", LocalDateTime.now(), false, "Me");
        List<MessageResponseDTO> messages = Arrays.asList(msg1, msg2);
        when(messageService.getMessagesByConversationId(5L)).thenReturn(messages);

        // When
        ResponseEntity<List<MessageResponseDTO>> response = messageController.getMessagesByConversation(5L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getMessagesByConversation_shouldReturnEmptyList() {
        // Given
        when(messageService.getMessagesByConversationId(1L)).thenReturn(List.of());

        // When
        ResponseEntity<List<MessageResponseDTO>> response = messageController.getMessagesByConversation(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void getMessagesByConversation_shouldReturnBadRequest_whenIdNull() {
        // When
        ResponseEntity<List<MessageResponseDTO>> response = messageController.getMessagesByConversation(null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getMessagesByConversation_shouldReturnBadRequest_whenException() {
        // Given
        when(messageService.getMessagesByConversationId(1L)).thenThrow(new RuntimeException("Error"));

        // When
        ResponseEntity<List<MessageResponseDTO>> response = messageController.getMessagesByConversation(1L);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnUpdatedMessage() {
        // Given
        MessageDTO input = new MessageDTO();
        input.setContent("Updated");
        
        MessageDTO updated = new MessageDTO();
        updated.setId(1L);
        updated.setContent("Updated");
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class))).thenReturn(updated);

        // When
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void updateMessage_shouldReturnNotFound_whenNotFound() {
        // Given
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class)))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException("not found"));
        MessageDTO input = new MessageDTO();
        
        // When
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnForbidden_whenForbidden() {
        // Given
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class)))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException("forbidden"));
        MessageDTO input = new MessageDTO();
        
        // When
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnBadRequest_whenIdNull() {
        // Given
        MessageDTO message = new MessageDTO();
        message.setContent("updated");
        
        // When
        ResponseEntity<MessageDTO> response = messageController.updateMessage(null, message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnBadRequest_whenMessageNull() {
        // When
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnBadRequest_whenException() {
        // Given
        MessageDTO message = new MessageDTO();
        message.setContent("test");
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class)))
                .thenThrow(new RuntimeException("Error"));
        
        // When
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 