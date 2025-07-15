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

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private Principal principal;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(principal.getName()).thenReturn("9"); // Simule un userId = 9
    }

    @Test
    void sendMessage_shouldReturnSavedMessage() {
        MessageDTO input = new MessageDTO();
        input.setContent("Hello");
        MessageDTO saved = new MessageDTO();
        saved.setId(1L);
        saved.setContent("Hello");
        saved.setSenderId(9L);
        when(messageService.sendMessage(any(MessageDTO.class))).thenReturn(saved);

        ResponseEntity<MessageDTO> response = messageController.sendMessage(input, principal);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved, response.getBody());
        assertEquals(9L, response.getBody().getSenderId());
    }

    @Test
    void getMessagesByConversation_shouldReturnMessages() {
        MessageDTO msg1 = new MessageDTO();
        msg1.setId(1L);
        MessageDTO msg2 = new MessageDTO();
        msg2.setId(2L);
        List<MessageDTO> messages = Arrays.asList(msg1, msg2);
        when(messageService.getMessagesByConversationId(5L, 9L)).thenReturn(messages);

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesByConversation(5L, principal);
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
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class), eq(9L))).thenReturn(updated);

        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input, principal);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void updateMessage_shouldReturnNotFound_whenNotFound() {
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class), eq(9L)))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException("not found"));
        MessageDTO input = new MessageDTO();
        
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input, principal);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateMessage_shouldReturnForbidden_whenForbidden() {
        when(messageService.updateMessage(eq(1L), any(MessageDTO.class), eq(9L)))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException("forbidden"));
        MessageDTO input = new MessageDTO();
        
        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, input, principal);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
} 