package com.mastere_project.vacances_tranquilles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.UpdateReservationStatusDTO;
import com.mastere_project.vacances_tranquilles.exception.ApplicationControllerAdvice;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UnauthorizedReservationAccessException;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        ApplicationControllerAdvice applicationControllerAdvice = new ApplicationControllerAdvice();

        mockMvc = MockMvcBuilders.standaloneSetup(reservationController)
                .setControllerAdvice(applicationControllerAdvice)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllReservations_shouldReturnReservationsList() throws Exception {
        List<ReservationResponseDTO> reservations = Arrays.asList(
                createSampleReservationResponseDTO(1L, "PENDING"),
                createSampleReservationResponseDTO(2L, "IN_PROGRESS"));

        when(reservationService.getAllReservations()).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void getReservationById_shouldReturnReservation() throws Exception {
        ReservationResponseDTO reservation = createSampleReservationResponseDTO(1L, "PENDING");

        when(reservationService.getReservationById(1L)).thenReturn(reservation);

        mockMvc.perform(get("/api/reservations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(reservationService, times(1)).getReservationById(1L);
    }

    @Test
    void getReservationById_whenReservationNotFound_shouldReturnNotFound() throws Exception {
        when(reservationService.getReservationById(999L))
                .thenThrow(new ReservationNotFoundException("Réservation introuvable"));

        mockMvc.perform(get("/api/reservations/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).getReservationById(999L);
    }

    @Test
    void updateStatus_shouldReturnUpdatedReservation() throws Exception {
        ReservationResponseDTO updatedReservation = createSampleReservationResponseDTO(1L, "IN_PROGRESS");
        UpdateReservationStatusDTO updateDTO = createSampleUpdateStatusDTO("IN_PROGRESS");

        when(reservationService.changeStatusOfReservationByProvider(1L, updateDTO)).thenReturn(updatedReservation);

        mockMvc.perform(patch("/api/reservations/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(reservationService, times(1)).changeStatusOfReservationByProvider(1L, updateDTO);
    }

    @Test
    void updateStatus_whenReservationNotFound_shouldReturnNotFound() throws Exception {
        UpdateReservationStatusDTO updateDTO = createSampleUpdateStatusDTO("IN_PROGRESS");

        when(reservationService.changeStatusOfReservationByProvider(999L, updateDTO))
                .thenThrow(new ReservationNotFoundException("Réservation introuvable"));

        mockMvc.perform(patch("/api/reservations/999/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).changeStatusOfReservationByProvider(999L, updateDTO);
    }

    @Test
    void createReservation_shouldReturnCreatedReservation() throws Exception {
        ReservationDTO createDTO = createSampleReservationCreateDTO();
        ReservationResponseDTO createdReservation = createSampleReservationResponseDTO(1L, "PENDING");

        when(reservationService.createReservation(createDTO)).thenReturn(createdReservation);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(reservationService, times(1)).createReservation(createDTO);
    }

    @Test
    void createReservation_whenError_shouldReturnForbidden() throws Exception {
        ReservationDTO createDTO = createSampleReservationCreateDTO();

        when(reservationService.createReservation(createDTO))
                .thenThrow(new UnauthorizedReservationAccessException(
                        "Vous n'êtes pas autorisé à créer cette réservation"));

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isForbidden());

        verify(reservationService, times(1)).createReservation(createDTO);
    }

    private ReservationDTO createSampleReservationCreateDTO() {
        ReservationDTO dto = new ReservationDTO();
        dto.setClientId(1L);
        dto.setProviderId(2L);
        dto.setServiceId(1L);
        dto.setPaymentId(1L);
        dto.setReservationDate(LocalDateTime.now().plusDays(1));
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusHours(2));
        dto.setTotalPrice(new BigDecimal("100.0"));

        return dto;
    }

    private ReservationResponseDTO createSampleReservationResponseDTO(Long id, String status) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(id);
        dto.setStatus(ReservationStatus.valueOf(status.toUpperCase()));
        dto.setReservationDate(LocalDateTime.now());
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusHours(2));
        dto.setTotalPrice(new BigDecimal("100.0"));
        dto.setServiceId(1L);
        dto.setServiceName("Test Service");
        dto.setServiceDescription("Test Description");

        return dto;
    }

    private UpdateReservationStatusDTO createSampleUpdateStatusDTO(String status) {
        UpdateReservationStatusDTO dto = new UpdateReservationStatusDTO();
        dto.setStatus(ReservationStatus.valueOf(status.toUpperCase()));
        return dto;
    }
}