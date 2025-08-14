package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageResponseDTO;
import com.mastere_project.vacances_tranquilles.exception.ErrorEntity;
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
        ResponseEntity<Object> response = messageController.sendMessage(input);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    void sendMessage_shouldReturnBadRequest_whenException() {
        // Given
        MessageDTO message = new MessageDTO();
        message.setConversationId(1L);
        message.setContent("test");
        when(messageService.sendMessage(message)).thenThrow(new RuntimeException("Error"));
        
        // When
        ResponseEntity<Object> response = messageController.sendMessage(message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorEntity);
        ErrorEntity error = (ErrorEntity) response.getBody();
        assertEquals("Error", error.getMessage());
    }

    @Test
    void getMessagesByConversation_shouldReturnMessages() {
        // Given
        MessageResponseDTO msg1 = new MessageResponseDTO(1L, "User1", "Hello", LocalDateTime.now(), true, "Me");
        MessageResponseDTO msg2 = new MessageResponseDTO(2L, "User2", "Hi", LocalDateTime.now(), false, "Me");
        List<MessageResponseDTO> messages = Arrays.asList(msg1, msg2);
        when(messageService.getMessagesByConversationId(5L)).thenReturn(messages);

        // When
        ResponseEntity<Object> response = messageController.getMessagesByConversation(5L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        List<MessageResponseDTO> responseMessages = (List<MessageResponseDTO>) response.getBody();
        assertEquals(2, responseMessages.size());
    }

    @Test
    void getMessagesByConversation_shouldReturnEmptyList() {
        // Given
        when(messageService.getMessagesByConversationId(1L)).thenReturn(List.of());

        // When
        ResponseEntity<Object> response = messageController.getMessagesByConversation(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        List<MessageResponseDTO> responseMessages = (List<MessageResponseDTO>) response.getBody();
        assertEquals(0, responseMessages.size());
    }

    @Test
    void getMessagesByConversation_shouldReturnBadRequest_whenException() {
        // Given
        when(messageService.getMessagesByConversationId(1L)).thenThrow(new RuntimeException("Error"));

        // When
        ResponseEntity<Object> response = messageController.getMessagesByConversation(1L);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorEntity);
        ErrorEntity error = (ErrorEntity) response.getBody();
        assertEquals("Error", error.getMessage());
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
        ResponseEntity<Object> response = messageController.updateMessage(1L, input);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void updateMessage_shouldReturnBadRequest_whenException() {
        // Given
        MessageDTO message = new MessageDTO();
        message.setContent("test");
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class)))
                .thenThrow(new RuntimeException("Error"));
        
        // When
        ResponseEntity<Object> response = messageController.updateMessage(1L, message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorEntity);
        ErrorEntity error = (ErrorEntity) response.getBody();
        assertEquals("Error", error.getMessage());
    }
} 