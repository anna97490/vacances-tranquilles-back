package com.mastere_project.vacances_tranquilles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.SimpleUserDTO;
import com.mastere_project.vacances_tranquilles.dto.SimpleServiceDTO;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private ReservationService reservationService;
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reservationService = mock(ReservationService.class);
        ReservationController reservationController = new ReservationController(reservationService);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUserReservations_shouldReturnReservationsList() throws Exception {
        // Arrange
        List<ReservationDTO> reservations = Arrays.asList(
            createSampleReservationDTO(1L, "PENDING"),
            createSampleReservationDTO(2L, "IN_PROGRESS")
        );
        when(reservationService.getReservationsForUserId(1L)).thenReturn(reservations);

        // Act & Assert
        mockMvc.perform(get("/api/reservations")
                .principal(() -> "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(reservationService, times(1)).getReservationsForUserId(1L);
    }

    @Test
    void getReservationById_shouldReturnReservation() throws Exception {
        // Arrange
        ReservationDTO reservation = createSampleReservationDTO(1L, "PENDING");
        when(reservationService.getReservationByIdAndUserId(1L, 1L)).thenReturn(reservation);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/1")
                .principal(() -> "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(reservationService, times(1)).getReservationByIdAndUserId(1L, 1L);
    }

    @Test
    void getReservationById_whenReservationNotFound_shouldReturnNotFound() throws Exception {
        // Arrange
        when(reservationService.getReservationByIdAndUserId(999L, 1L))
                .thenThrow(new RuntimeException("Reservation not found"));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/999")
                .principal(() -> "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).getReservationByIdAndUserId(999L, 1L);
    }

    @Test
    void getReservationsByStatus_shouldReturnFilteredReservations() throws Exception {
        // Arrange
        List<ReservationDTO> reservations = Arrays.asList(
            createSampleReservationDTO(1L, "PENDING"),
            createSampleReservationDTO(2L, "PENDING")
        );
        when(reservationService.getReservationsByStatus(1L, "PENDING")).thenReturn(reservations);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/status/pending")
                .principal(() -> "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("PENDING"));

        verify(reservationService, times(1)).getReservationsByStatus(1L, "PENDING");
    }

    @Test
    void getReservationByIdAndStatus_shouldReturnReservation() throws Exception {
        // Arrange
        ReservationDTO reservation = createSampleReservationDTO(1L, "PENDING");
        when(reservationService.getReservationByIdAndUserIdAndStatus(1L, 1L, "PENDING"))
                .thenReturn(reservation);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/1/status/pending")
                .principal(() -> "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(reservationService, times(1)).getReservationByIdAndUserIdAndStatus(1L, 1L, "PENDING");
    }

    @Test
    void getReservationByIdAndStatus_whenReservationNotFound_shouldReturnNotFound() throws Exception {
        // Arrange
        when(reservationService.getReservationByIdAndUserIdAndStatus(999L, 1L, "PENDING"))
                .thenThrow(new RuntimeException("Reservation not found"));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/999/status/pending")
                .principal(() -> "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).getReservationByIdAndUserIdAndStatus(999L, 1L, "PENDING");
    }

    @Test
    void acceptReservation_shouldReturnUpdatedReservation() throws Exception {
        // Arrange
        ReservationDTO updatedReservation = createSampleReservationDTO(1L, "IN_PROGRESS");
        when(reservationService.acceptReservationByProvider(1L, 2L)).thenReturn(updatedReservation);

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/1/accept")
                .principal(() -> "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(reservationService, times(1)).acceptReservationByProvider(1L, 2L);
    }

    @Test
    void acceptReservation_whenReservationNotFound_shouldReturnNotFound() throws Exception {
        // Arrange
        when(reservationService.acceptReservationByProvider(999L, 2L))
                .thenThrow(new RuntimeException("Reservation not found"));

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/999/accept")
                .principal(() -> "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).acceptReservationByProvider(999L, 2L);
    }

    @Test
    void completeReservation_shouldReturnUpdatedReservation() throws Exception {
        // Arrange
        ReservationDTO updatedReservation = createSampleReservationDTO(1L, "CLOSED");
        when(reservationService.completeReservationByProvider(1L, 2L)).thenReturn(updatedReservation);

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/1/complete")
                .principal(() -> "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CLOSED"));

        verify(reservationService, times(1)).completeReservationByProvider(1L, 2L);
    }

    @Test
    void completeReservation_whenReservationNotFound_shouldReturnNotFound() throws Exception {
        // Arrange
        when(reservationService.completeReservationByProvider(999L, 2L))
                .thenThrow(new RuntimeException("Reservation not found"));

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/999/complete")
                .principal(() -> "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).completeReservationByProvider(999L, 2L);
    }

    private ReservationDTO createSampleReservationDTO(Long id, String status) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(id);
        dto.setStatus(ReservationStatus.valueOf(status.toUpperCase()));
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusDays(1));
        dto.setTotalPrice(100.0);
        
        // Créer les objets imbriqués
        SimpleUserDTO client = new SimpleUserDTO();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUserRole("CLIENT");
        client.setAddress("123 Main St");
        client.setCity("Paris");
        client.setPostalCode("75001");
        
        SimpleUserDTO provider = new SimpleUserDTO();
        provider.setId(2L);
        provider.setFirstName("Jane");
        provider.setLastName("Smith");
        provider.setUserRole("PROVIDER");
        provider.setAddress("456 Oak Ave");
        provider.setCity("Lyon");
        provider.setPostalCode("69001");
        
        SimpleServiceDTO service = new SimpleServiceDTO();
        service.setId(1L);
        service.setTitle("Service Test");
        service.setDescription("Description du service");
        service.setPrice(100.0);
        service.setProviderId(2L);
        
        dto.setClient(client);
        dto.setProvider(provider);
        dto.setService(service);
        
        return dto;
    }
} 