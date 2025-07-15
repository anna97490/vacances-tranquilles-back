package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationWithMessagesDTO;
import com.mastere_project.vacances_tranquilles.dto.MessageDTO;
import com.mastere_project.vacances_tranquilles.service.ConversationService;
import com.mastere_project.vacances_tranquilles.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConversationControllerTest {

    @Mock
    private ConversationService conversationService;

    @Mock
    private MessageService messageService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ConversationController conversationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(principal.getName()).thenReturn("9"); // Simule un userId = 9
    }

    @Test
    void getConversations_shouldReturnList() {
        ConversationDTO c1 = new ConversationDTO();
        c1.setId(1L);
        ConversationDTO c2 = new ConversationDTO();
        c2.setId(2L);
        List<ConversationDTO> conversations = Arrays.asList(c1, c2);
        when(conversationService.getConversationsForUser(9L)).thenReturn(conversations);

        ResponseEntity<List<ConversationDTO>> response = conversationController.getConversations(principal);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createConversation_shouldReturnCreated() {
        ConversationCreateRequest req = new ConversationCreateRequest();
        req.setOtherUserId(10L);
        req.setStatus("in_progress");
        ConversationDTO created = new ConversationDTO();
        created.setId(3L);
        when(conversationService.createConversation(9L, 10L)).thenReturn(created);

        ResponseEntity<ConversationDTO> response = conversationController.createConversation(req, principal);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(created, response.getBody());
    }

    @Test
    void createConversation_shouldReturnBadRequest_whenStatusInvalid() {
        ConversationCreateRequest req = new ConversationCreateRequest();
        req.setOtherUserId(10L);
        req.setStatus("done");
        
        ResponseEntity<ConversationDTO> response = conversationController.createConversation(req, principal);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createConversation_shouldReturnBadRequest_whenException() {
        ConversationCreateRequest req = new ConversationCreateRequest();
        req.setOtherUserId(10L);
        req.setStatus("in_progress");
        when(conversationService.createConversation(9L, 10L)).thenThrow(new RuntimeException("error"));
        
        ResponseEntity<ConversationDTO> response = conversationController.createConversation(req, principal);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getConversationWithMessages_shouldReturnOk() {
        ConversationDTO conv = new ConversationDTO();
        conv.setId(2L);
        List<MessageDTO> messages = Arrays.asList(new MessageDTO(), new MessageDTO());
        when(conversationService.getConversationById(2L, 9L)).thenReturn(conv);
        when(messageService.getMessagesByConversationId(2L, 9L)).thenReturn(messages);

        ResponseEntity<ConversationWithMessagesDTO> response = conversationController.getConversationWithMessages(2L, principal);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conv, response.getBody().getConversation());
        assertEquals(2, response.getBody().getMessages().size());
    }

    @Test
    void getConversationWithMessages_shouldReturnNotFound() {
        when(conversationService.getConversationById(2L, 9L))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException("not found"));
        
                ResponseEntity<ConversationWithMessagesDTO> response = conversationController.getConversationWithMessages(2L, principal);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getConversationWithMessages_shouldReturnForbidden() {
        when(conversationService.getConversationById(2L, 9L))
                .thenThrow(new com.mastere_project.vacances_tranquilles.exception.ConversationForbiddenException("forbidden"));
        
                ResponseEntity<ConversationWithMessagesDTO> response = conversationController.getConversationWithMessages(2L, principal);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
} 