package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ConversationDTO;
import com.mastere_project.vacances_tranquilles.dto.ConversationSummaryDto;
import com.mastere_project.vacances_tranquilles.service.ConversationService;
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

class ConversationControllerTest {

    @Mock
    private ConversationService conversationService;

    @InjectMocks
    private ConversationController conversationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getConversations_shouldReturnList() {
        ConversationSummaryDto c1 = new ConversationSummaryDto(1L, "User1", "Service1", null, null);
        ConversationSummaryDto c2 = new ConversationSummaryDto(2L, "User2", "Service2", null, null);
        List<ConversationSummaryDto> conversations = Arrays.asList(c1, c2);
        when(conversationService.getConversationsForUser()).thenReturn(conversations);

        ResponseEntity<List<ConversationSummaryDto>> response = conversationController.getConversations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getConversations_shouldReturnEmptyList() {
        when(conversationService.getConversationsForUser()).thenReturn(List.of());

        ResponseEntity<List<ConversationSummaryDto>> response = conversationController.getConversations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
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
    void createConversation_shouldReturnBadRequest_whenOtherUserIdNull() {
        ConversationCreateRequest req = new ConversationCreateRequest();
        req.setOtherUserId(null);
        req.setReservationId(1L);

        ResponseEntity<Object> response = conversationController.createConversation(req);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createConversation_shouldReturnBadRequest_whenReservationIdNull() {
        ConversationCreateRequest req = new ConversationCreateRequest();
        req.setOtherUserId(10L);
        req.setReservationId(null);

        ResponseEntity<Object> response = conversationController.createConversation(req);
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

    @Test
    void getConversationById_shouldReturnBadRequest_whenIdNull() {
        ResponseEntity<Object> response = conversationController.getConversationById(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 