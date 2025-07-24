package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.ReservationMapper;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getReservationsForUserId should return user reservations")
    void getReservationsForUserId_shouldReturnUserReservations() {
        // Arrange
        Long userId = 1L;
        List<Reservation> reservations = Arrays.asList(
            createSampleReservation(1L, ReservationStatus.PENDING),
            createSampleReservation(2L, ReservationStatus.IN_PROGRESS)
        );
        List<ReservationDTO> expectedDtos = Arrays.asList(
            createSampleReservationDTO(1L, ReservationStatus.PENDING),
            createSampleReservationDTO(2L, ReservationStatus.IN_PROGRESS)
        );

        when(reservationRepository.findByClientIdOrProviderId(userId, userId)).thenReturn(reservations);
        when(reservationMapper.toDTO(any(Reservation.class)))
            .thenReturn(expectedDtos.get(0), expectedDtos.get(1));

        // Act
        List<ReservationDTO> result = reservationService.getReservationsForUserId(userId);

        // Assert
        assertThat(result).hasSize(2);
        verify(reservationRepository).findByClientIdOrProviderId(userId, userId);
        verify(reservationMapper, times(2)).toDTO(any(Reservation.class));
    }

    @Test
    @DisplayName("getReservationByIdAndUserId should return reservation when user is authorized")
    void getReservationByIdAndUserId_shouldReturnReservation_whenUserAuthorized() {
        // Arrange
        Long reservationId = 1L;
        Long userId = 1L;
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);
        ReservationDTO expectedDto = createSampleReservationDTO(reservationId, ReservationStatus.PENDING);

        when(reservationRepository.findByIdAndClientIdOrIdAndProviderId(reservationId, userId, reservationId, userId))
            .thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(expectedDto);

        // Act
        ReservationDTO result = reservationService.getReservationByIdAndUserId(reservationId, userId);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
        verify(reservationRepository).findByIdAndClientIdOrIdAndProviderId(reservationId, userId, reservationId, userId);
        verify(reservationMapper).toDTO(reservation);
    }

    @Test
    @DisplayName("getReservationByIdAndUserId should throw exception when reservation not found")
    void getReservationByIdAndUserId_shouldThrowException_whenReservationNotFound() {
        // Arrange
        Long reservationId = 999L;
        Long userId = 1L;

        when(reservationRepository.findByIdAndClientIdOrIdAndProviderId(reservationId, userId, reservationId, userId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.getReservationByIdAndUserId(reservationId, userId))
            .isInstanceOf(ReservationNotFoundException.class)
            .hasMessage("Réservation introuvable ou non autorisée.");
    }

    @Test
    @DisplayName("getReservationsByStatus should return filtered reservations")
    void getReservationsByStatus_shouldReturnFilteredReservations() {
        // Arrange
        Long userId = 1L;
        String status = "PENDING";
        List<Reservation> clientReservations = Arrays.asList(
            createSampleReservation(1L, ReservationStatus.PENDING)
        );
        List<Reservation> providerReservations = Arrays.asList(
            createSampleReservation(2L, ReservationStatus.PENDING)
        );
        List<ReservationDTO> expectedDtos = Arrays.asList(
            createSampleReservationDTO(1L, ReservationStatus.PENDING),
            createSampleReservationDTO(2L, ReservationStatus.PENDING)
        );

        when(reservationRepository.findByStatusAndClientId(ReservationStatus.PENDING, userId))
            .thenReturn(clientReservations);
        when(reservationRepository.findByStatusAndProviderId(ReservationStatus.PENDING, userId))
            .thenReturn(providerReservations);
        when(reservationMapper.toDTO(any(Reservation.class)))
            .thenReturn(expectedDtos.get(0), expectedDtos.get(1));

        // Act
        List<ReservationDTO> result = reservationService.getReservationsByStatus(userId, status);

        // Assert
        assertThat(result).hasSize(2);
        verify(reservationRepository).findByStatusAndClientId(ReservationStatus.PENDING, userId);
        verify(reservationRepository).findByStatusAndProviderId(ReservationStatus.PENDING, userId);
        verify(reservationMapper, times(2)).toDTO(any(Reservation.class));
    }

    @Test
    @DisplayName("getReservationsByStatus should throw exception for invalid status")
    void getReservationsByStatus_shouldThrowException_whenInvalidStatus() {
        // Arrange
        Long userId = 1L;
        String invalidStatus = "INVALID_STATUS";

        // Act & Assert
        assertThatThrownBy(() -> reservationService.getReservationsByStatus(userId, invalidStatus))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Statut de réservation invalide");
    }

    @Test
    @DisplayName("getReservationByIdAndUserIdAndStatus should return reservation when authorized and status matches")
    void getReservationByIdAndUserIdAndStatus_shouldReturnReservation_whenAuthorizedAndStatusMatches() {
        // Arrange
        Long reservationId = 1L;
        Long userId = 1L;
        String status = "PENDING";
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);
        ReservationDTO expectedDto = createSampleReservationDTO(reservationId, ReservationStatus.PENDING);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(expectedDto);

        // Act
        ReservationDTO result = reservationService.getReservationByIdAndUserIdAndStatus(reservationId, userId, status);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
        verify(reservationRepository).findById(reservationId);
        verify(reservationMapper).toDTO(reservation);
    }

    @Test
    @DisplayName("getReservationByIdAndUserIdAndStatus should throw exception when reservation not found")
    void getReservationByIdAndUserIdAndStatus_shouldThrowException_whenReservationNotFound() {
        // Arrange
        Long reservationId = 999L;
        Long userId = 1L;
        String status = "PENDING";

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.getReservationByIdAndUserIdAndStatus(reservationId, userId, status))
            .isInstanceOf(ReservationNotFoundException.class)
            .hasMessage("Réservation introuvable");
    }

    @Test
    @DisplayName("getReservationByIdAndUserIdAndStatus should throw exception when user not authorized")
    void getReservationByIdAndUserIdAndStatus_shouldThrowException_whenUserNotAuthorized() {
        // Arrange
        Long reservationId = 1L;
        Long userId = 999L; // User not associated with reservation
        String status = "PENDING";
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.getReservationByIdAndUserIdAndStatus(reservationId, userId, status))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Vous n'avez pas accès à cette réservation");
    }

    @Test
    @DisplayName("getReservationByIdAndUserIdAndStatus should throw exception when status doesn't match")
    void getReservationByIdAndUserIdAndStatus_shouldThrowException_whenStatusDoesntMatch() {
        // Arrange
        Long reservationId = 1L;
        Long userId = 1L;
        String status = "CLOSED";
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.getReservationByIdAndUserIdAndStatus(reservationId, userId, status))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Le statut de la réservation ne correspond pas à celui demandé");
    }

    @Test
    @DisplayName("acceptReservationByProvider should update status to IN_PROGRESS")
    void acceptReservationByProvider_shouldUpdateStatusToInProgress() {
        // Arrange
        Long reservationId = 1L;
        Long providerId = 2L;
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);
        Reservation updatedReservation = createSampleReservation(reservationId, ReservationStatus.IN_PROGRESS);
        ReservationDTO expectedDto = createSampleReservationDTO(reservationId, ReservationStatus.IN_PROGRESS);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);
        when(reservationMapper.toDTO(updatedReservation)).thenReturn(expectedDto);

        // Act
        ReservationDTO result = reservationService.acceptReservationByProvider(reservationId, providerId);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).save(reservation);
        verify(reservationMapper).toDTO(updatedReservation);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("acceptReservationByProvider should throw exception when provider not authorized")
    void acceptReservationByProvider_shouldThrowException_whenProviderNotAuthorized() {
        // Arrange
        Long reservationId = 1L;
        Long providerId = 999L; // Different provider
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.acceptReservationByProvider(reservationId, providerId))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Vous n'êtes pas autorisé à accepter cette réservation");
    }

    @Test
    @DisplayName("completeReservationByProvider should update status to CLOSED")
    void completeReservationByProvider_shouldUpdateStatusToClosed() {
        // Arrange
        Long reservationId = 1L;
        Long providerId = 2L;
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.IN_PROGRESS);
        Reservation updatedReservation = createSampleReservation(reservationId, ReservationStatus.CLOSED);
        ReservationDTO expectedDto = createSampleReservationDTO(reservationId, ReservationStatus.CLOSED);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);
        when(reservationMapper.toDTO(updatedReservation)).thenReturn(expectedDto);

        // Act
        ReservationDTO result = reservationService.completeReservationByProvider(reservationId, providerId);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).save(reservation);
        verify(reservationMapper).toDTO(updatedReservation);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CLOSED);
    }

    @Test
    @DisplayName("completeReservationByProvider should throw exception when provider not authorized")
    void completeReservationByProvider_shouldThrowException_whenProviderNotAuthorized() {
        // Arrange
        Long reservationId = 1L;
        Long providerId = 999L; // Different provider
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.IN_PROGRESS);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.completeReservationByProvider(reservationId, providerId))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Vous n'êtes pas autorisé à clôturer cette réservation");
    }

    private Reservation createSampleReservation(Long id, ReservationStatus status) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setStatus(status);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStartDate(LocalDateTime.now().plusDays(1));
        reservation.setEndDate(LocalDateTime.now().plusDays(2));
        reservation.setComment("Test reservation");
        reservation.setTotalPrice(100.0);

        // Create client
        User client = new User();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUserRole(UserRole.CLIENT);
        reservation.setClient(client);

        // Create provider
        User provider = new User();
        provider.setId(2L);
        provider.setFirstName("Jane");
        provider.setLastName("Smith");
        provider.setUserRole(UserRole.PROVIDER);
        reservation.setProvider(provider);

        // Create service
        Service service = new Service();
        service.setId(1L);
        service.setTitle("Test Service");
        service.setDescription("Test Description");
        service.setPrice(100.0);
        service.setProvider(provider);
        reservation.setService(service);

        return reservation;
    }

    private ReservationDTO createSampleReservationDTO(Long id, ReservationStatus status) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(id);
        dto.setStatus(status);
        dto.setReservationDate(LocalDateTime.now());
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));
        dto.setComment("Test reservation");
        dto.setTotalPrice(100.0);
        return dto;
    }
} 