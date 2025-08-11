package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitaires pour ReservationMapperImpl.
 * Vérifie la conversion correcte entre entités Reservation et DTOs.
 */
class ReservationMapperImplTest {

    private ReservationMapperImpl reservationMapper;

    @BeforeEach
    void setUp() {
        reservationMapper = new ReservationMapperImpl();
    }

    @Test
    @DisplayName("toDTO should return null when reservation is null")
    void toDTO_shouldReturnNull_whenReservationIsNull() {
        ReservationDTO result = reservationMapper.toDTO(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("toDTO should map reservation to DTO correctly")
    void toDTO_shouldMapReservationToDTOCorrectly() {
        Reservation reservation = createSampleReservation();

        ReservationDTO result = reservationMapper.toDTO(reservation);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reservation.getId());
        assertThat(result.getStatus()).isEqualTo(reservation.getStatus());
        assertThat(result.getTotalPrice()).isEqualTo(reservation.getTotalPrice());
        
        // Vérifier les dates
        LocalDateTime expectedReservationDate = reservation.getReservationDate().atStartOfDay();
        LocalDateTime expectedStartDate = reservation.getReservationDate().atTime(reservation.getStartDate());
        LocalDateTime expectedEndDate = reservation.getReservationDate().atTime(reservation.getEndDate());
        
        assertThat(result.getReservationDate()).isEqualTo(expectedReservationDate);
        assertThat(result.getStartDate()).isEqualTo(expectedStartDate);
        assertThat(result.getEndDate()).isEqualTo(expectedEndDate);
    }

    @Test
    @DisplayName("toResponseDTO should return null when reservation is null")
    void toResponseDTO_shouldReturnNull_whenReservationIsNull() {
        ReservationResponseDTO result = reservationMapper.toResponseDTO(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("toResponseDTO should map reservation to response DTO correctly")
    void toResponseDTO_shouldMapReservationToResponseDTOCorrectly() {
        Reservation reservation = createSampleReservation();

        ReservationResponseDTO result = reservationMapper.toResponseDTO(reservation);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reservation.getId());
        assertThat(result.getStatus()).isEqualTo(reservation.getStatus());
        assertThat(result.getTotalPrice()).isEqualTo(reservation.getTotalPrice());
        
        // Vérifier les dates
        LocalDateTime expectedReservationDate = reservation.getReservationDate().atStartOfDay();
        LocalDateTime expectedStartDate = reservation.getReservationDate().atTime(reservation.getStartDate());
        LocalDateTime expectedEndDate = reservation.getReservationDate().atTime(reservation.getEndDate());
        
        assertThat(result.getReservationDate()).isEqualTo(expectedReservationDate);
        assertThat(result.getStartDate()).isEqualTo(expectedStartDate);
        assertThat(result.getEndDate()).isEqualTo(expectedEndDate);
    }

    @Test
    @DisplayName("toDTO should handle null reservationDate and return null date-times")
    void toDTO_shouldHandleNullDates() {
        Reservation reservation = createSampleReservation();
        reservation.setReservationDate(null);

        ReservationDTO result = reservationMapper.toDTO(reservation);

        assertThat(result.getReservationDate()).isNull();
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
    }

    @Test
    @DisplayName("toResponseDTO should handle null reservationDate and return null date-times")
    void toResponseDTO_shouldHandleNullDates() {
        Reservation reservation = createSampleReservation();
        reservation.setReservationDate(null);

        ReservationResponseDTO result = reservationMapper.toResponseDTO(reservation);

        assertThat(result.getReservationDate()).isNull();
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
    }

    @Test
    @DisplayName("toResponseDTO should map client and provider information correctly")
    void toResponseDTO_shouldMapClientAndProviderInfo() {
        Reservation reservation = createSampleReservation();

        ReservationResponseDTO result = reservationMapper.toResponseDTO(reservation);

        assertThat(result.getClientId()).isEqualTo(1L);
        assertThat(result.getClientName()).isEqualTo("John Doe");
        assertThat(result.getProviderId()).isEqualTo(2L);
        assertThat(result.getProviderName()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("toDTO and toResponseDTO should produce same basic fields")
    void toDTOAndToResponseDTO_shouldProduceSameBasicFields() {
        Reservation reservation = createSampleReservation();

        ReservationDTO dto = reservationMapper.toDTO(reservation);
        ReservationResponseDTO responseDTO = reservationMapper.toResponseDTO(reservation);

        assertThat(dto.getId()).isEqualTo(responseDTO.getId());
        assertThat(dto.getStatus()).isEqualTo(responseDTO.getStatus());
        assertThat(dto.getTotalPrice()).isEqualTo(responseDTO.getTotalPrice());
        assertThat(dto.getReservationDate()).isEqualTo(responseDTO.getReservationDate());
        assertThat(dto.getStartDate()).isEqualTo(responseDTO.getStartDate());
        assertThat(dto.getEndDate()).isEqualTo(responseDTO.getEndDate());
    }

    @Test
    @DisplayName("toDTO should handle different reservation statuses")
    void toDTO_shouldHandleDifferentReservationStatuses() {
        Reservation pendingReservation = createSampleReservation();
        pendingReservation.setStatus(ReservationStatus.PENDING);
        
        Reservation inProgressReservation = createSampleReservation();
        inProgressReservation.setStatus(ReservationStatus.IN_PROGRESS);
        
        Reservation closedReservation = createSampleReservation();
        closedReservation.setStatus(ReservationStatus.CLOSED);

        ReservationDTO pendingDTO = reservationMapper.toDTO(pendingReservation);
        ReservationDTO inProgressDTO = reservationMapper.toDTO(inProgressReservation);
        ReservationDTO closedDTO = reservationMapper.toDTO(closedReservation);

        assertThat(pendingDTO.getStatus()).isEqualTo(ReservationStatus.PENDING);
        assertThat(inProgressDTO.getStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
        assertThat(closedDTO.getStatus()).isEqualTo(ReservationStatus.CLOSED);
    }

    @Test
    @DisplayName("toResponseDTO should handle different reservation statuses")
    void toResponseDTO_shouldHandleDifferentReservationStatuses() {
        Reservation pendingReservation = createSampleReservation();
        pendingReservation.setStatus(ReservationStatus.PENDING);
        
        Reservation inProgressReservation = createSampleReservation();
        inProgressReservation.setStatus(ReservationStatus.IN_PROGRESS);
        
        Reservation closedReservation = createSampleReservation();
        closedReservation.setStatus(ReservationStatus.CLOSED);

        ReservationResponseDTO pendingDTO = reservationMapper.toResponseDTO(pendingReservation);
        ReservationResponseDTO inProgressDTO = reservationMapper.toResponseDTO(inProgressReservation);
        ReservationResponseDTO closedDTO = reservationMapper.toResponseDTO(closedReservation);

        assertThat(pendingDTO.getStatus()).isEqualTo(ReservationStatus.PENDING);
        assertThat(inProgressDTO.getStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
        assertThat(closedDTO.getStatus()).isEqualTo(ReservationStatus.CLOSED);
    }

    @Test
    @DisplayName("toDTO should handle different total prices")
    void toDTO_shouldHandleDifferentTotalPrices() {
        Reservation reservation = createSampleReservation();
        reservation.setTotalPrice(BigDecimal.valueOf(150.50));

        ReservationDTO result = reservationMapper.toDTO(reservation);

        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(150.50));
    }

    @Test
    @DisplayName("toResponseDTO should handle different total prices")
    void toResponseDTO_shouldHandleDifferentTotalPrices() {
        Reservation reservation = createSampleReservation();
        reservation.setTotalPrice(BigDecimal.valueOf(200.75));

        ReservationResponseDTO result = reservationMapper.toResponseDTO(reservation);

        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(200.75));
    }

    @Test
    @DisplayName("toResponseDTO should handle null client and provider")
    void toResponseDTO_shouldHandleNullClientAndProvider() {
        Reservation reservation = createSampleReservation();
        reservation.setClient(null);
        reservation.setProvider(null);

        ReservationResponseDTO result = reservationMapper.toResponseDTO(reservation);

        assertThat(result.getClientId()).isNull();
        assertThat(result.getClientName()).isNull();
        assertThat(result.getClientEmail()).isNull();
        assertThat(result.getProviderId()).isNull();
        assertThat(result.getProviderName()).isNull();
        assertThat(result.getProviderEmail()).isNull();
    }

    @Test
    @DisplayName("toResponseDTO should handle null conversation")
    void toResponseDTO_shouldHandleNullConversation() {
        Reservation reservation = createSampleReservation();
        reservation.setConversation(null);

        ReservationResponseDTO result = reservationMapper.toResponseDTO(reservation);

        assertThat(result.getConversationId()).isNull();
    }

    @Test
    @DisplayName("toResponseDTO should handle null startDate and endDate")
    void toResponseDTO_shouldHandleNullStartAndEndDates() {
        Reservation reservation = createSampleReservation();
        reservation.setStartDate(null);
        reservation.setEndDate(null);

        ReservationResponseDTO result = reservationMapper.toResponseDTO(reservation);

        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
        assertThat(result.getReservationDate()).isNotNull(); // reservationDate reste non-null
    }

    @Test
    @DisplayName("toDTO should handle null startDate and endDate")
    void toDTO_shouldHandleNullStartAndEndDates() {
        Reservation reservation = createSampleReservation();
        reservation.setStartDate(null);
        reservation.setEndDate(null);

        ReservationDTO result = reservationMapper.toDTO(reservation);

        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
        assertThat(result.getReservationDate()).isNotNull(); // reservationDate reste non-null
    }

    private Reservation createSampleReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservationDate(LocalDate.of(2024, 1, 15));
        reservation.setStartDate(LocalTime.of(10, 0));
        reservation.setEndDate(LocalTime.of(12, 0));
        reservation.setTotalPrice(BigDecimal.valueOf(100.0));

        // Créer un client
        User client = new User();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUserRole(UserRole.CLIENT);
        reservation.setClient(client);

        // Créer un prestataire
        User provider = new User();
        provider.setId(2L);
        provider.setFirstName("Jane");
        provider.setLastName("Smith");
        provider.setUserRole(UserRole.PROVIDER);
        reservation.setProvider(provider);

        // Créer un service
        Service service = new Service();
        service.setId(1L);
        service.setTitle("Test Service");
        service.setDescription("Test Description");
        service.setPrice(BigDecimal.valueOf(100.0));
        service.setProvider(provider);
        reservation.setService(service);

        return reservation;
    }
} 