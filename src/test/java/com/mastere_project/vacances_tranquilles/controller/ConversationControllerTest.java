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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.mastere_project.vacances_tranquilles.exception.ConversationNotFoundException;

class ConversationControllerTest {

    @Mock
    private ConversationService conversationService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ConversationController conversationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getConversations_shouldReturnList() {
        ConversationDTO c1 = new ConversationDTO();
        c1.setId(1L);
        ConversationDTO c2 = new ConversationDTO();
        c2.setId(2L);
        List<ConversationDTO> conversations = Arrays.asList(c1, c2);
        when(conversationService.getConversationsForUser()).thenReturn(conversations);

        ResponseEntity<List<ConversationDTO>> response = conversationController.getConversations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createConversation_shouldReturnCreated() {
        ConversationCreateRequest req = new ConversationCreateRequest();
        req.setOtherUserId(10L);
        req.setReservationId(1L);
        ConversationDTO created = new ConversationDTO();
        created.setId(3L);
        when(conversationService.createConversation(10L, 1L)).thenReturn(created);

        ResponseEntity<?> response = conversationController.createConversation(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(created, response.getBody());
    }

    @Test
    void createConversation_shouldReturnBadRequest_whenException() {
        ConversationCreateRequest req = new ConversationCreateRequest();
        req.setOtherUserId(10L);
        req.setReservationId(1L);
        when(conversationService.createConversation(10L, 1L)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = conversationController.createConversation(req);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getConversationWithMessages_shouldReturnOk() {
        ConversationDTO conv = new ConversationDTO();
        conv.setId(2L);
        List<MessageDTO> messages = Arrays.asList(new MessageDTO(), new MessageDTO());
        when(conversationService.getConversationById(2L)).thenReturn(conv);
        when(messageService.getMessagesByConversationId(2L)).thenReturn(messages);

        ResponseEntity<ConversationWithMessagesDTO> response = conversationController.getConversationWithMessages(2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conv, response.getBody().getConversation());
        assertEquals(2, response.getBody().getMessages().size());
    }

    @Test
    void getConversationWithMessages_shouldReturnBadRequest_whenException() {
        when(conversationService.getConversationById(99L))
                .thenThrow(new ConversationNotFoundException("Conversation not found: 99"));
        
        ResponseEntity<ConversationWithMessagesDTO> response = conversationController.getConversationWithMessages(99L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getConversationById_shouldReturnOk() {
        ConversationDTO conv = new ConversationDTO();
        conv.setId(2L);
        when(conversationService.getConversationById(2L)).thenReturn(conv);

        ResponseEntity<?> response = conversationController.getConversationById(2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conv, response.getBody());
    }

    @Test
    void getConversationById_shouldReturnBadRequest_whenException() {
        when(conversationService.getConversationById(9999L))
                .thenThrow(new RuntimeException("Conversation not found"));
        
        ResponseEntity<?> response = conversationController.getConversationById(9999L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 