package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.ReservationResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.UpdateReservationStatusDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.exception.ReservationNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.UnauthorizedReservationAccessException;
import com.mastere_project.vacances_tranquilles.mapper.ReservationMapper;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ReservationRepository;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    void setUp() {
        // Setup common mocks
    }

    @Test
    @DisplayName("getAllReservations should return user reservations")
    void getAllReservations_shouldReturnUserReservations() {
        // Arrange
        Long userId = 1L;
        User client = createSampleUser(userId, UserRole.CLIENT);
        List<Reservation> reservations = Arrays.asList(
                createSampleReservation(1L, ReservationStatus.PENDING));
        List<ReservationResponseDTO> expectedDtos = Arrays.asList(
                createSampleReservationResponseDTO(1L, ReservationStatus.PENDING));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(client));
            when(reservationRepository.findByClientId(userId)).thenReturn(reservations);
            when(reservationMapper.toResponseDTO(any(Reservation.class))).thenReturn(expectedDtos.get(0));

            List<ReservationResponseDTO> result = reservationService.getAllReservations();

            assertThat(result).hasSize(1);
            verify(userRepository).findById(userId);
            verify(reservationRepository).findByClientId(userId);
            verify(reservationMapper).toResponseDTO(any(Reservation.class));
        }
    }

    @Test
    @DisplayName("getReservationById should return reservation when user is authorized")
    void getReservationById_shouldReturnReservation_whenUserAuthorized() {
        Long reservationId = 1L;
        Long userId = 1L;
        User client = createSampleUser(userId, UserRole.CLIENT);
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);
        ReservationResponseDTO expectedDto = createSampleReservationResponseDTO(reservationId,
                ReservationStatus.PENDING);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(client));
            when(reservationRepository.findByIdAndUserId(reservationId, userId))
                    .thenReturn(Optional.of(reservation));
            when(reservationMapper.toResponseDTO(reservation)).thenReturn(expectedDto);

            ReservationResponseDTO result = reservationService.getReservationById(reservationId);

            assertEquals(expectedDto, result);
            verify(userRepository).findById(userId);
            verify(reservationRepository).findByIdAndUserId(reservationId, userId);
            verify(reservationMapper).toResponseDTO(reservation);
        }
    }

    @Test
    @DisplayName("getReservationById should throw exception when reservation not found")
    void getReservationById_shouldThrowException_whenReservationNotFound() {
        Long reservationId = 999L;
        Long userId = 1L;
        User client = createSampleUser(userId, UserRole.CLIENT);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(client));
            when(reservationRepository.findByIdAndUserId(reservationId, userId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> reservationService.getReservationById(reservationId))
                    .isInstanceOf(ReservationNotFoundException.class)
                    .hasMessage("Réservation introuvable ou non autorisée.");
        }
    }

    @Test
    @DisplayName("changeStatusOfReservationByProvider should update status to IN_PROGRESS")
    void changeStatusOfReservationByProvider_shouldUpdateStatusToInProgress() {
        Long reservationId = 1L;
        Long providerId = 2L;
        User provider = createSampleUser(providerId, UserRole.PROVIDER);
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);
        Reservation updatedReservation = createSampleReservation(reservationId, ReservationStatus.IN_PROGRESS);
        ReservationResponseDTO expectedDto = createSampleReservationResponseDTO(reservationId,
                ReservationStatus.IN_PROGRESS);
        UpdateReservationStatusDTO updateDTO = createSampleUpdateStatusDTO(ReservationStatus.IN_PROGRESS);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(providerId);
            when(userRepository.findById(providerId)).thenReturn(Optional.of(provider));
            when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
            when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);
            when(reservationMapper.toResponseDTO(updatedReservation)).thenReturn(expectedDto);

            ReservationResponseDTO result = reservationService.changeStatusOfReservationByProvider(reservationId,
                    updateDTO);

            assertThat(result).isEqualTo(expectedDto);
            verify(userRepository).findById(providerId);
            verify(reservationRepository).findById(reservationId);
            verify(reservationRepository).save(any(Reservation.class));
            verify(reservationMapper).toResponseDTO(updatedReservation);
        }
    }

    @Test
    @DisplayName("changeStatusOfReservationByProvider should throw exception when provider not authorized")
    void changeStatusOfReservationByProvider_shouldThrowException_whenProviderNotAuthorized() {
        Long reservationId = 1L;
        Long providerId = 2L;
        User provider = createSampleUser(providerId, UserRole.PROVIDER);
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.PENDING);
        User unauthorizedProvider = createSampleUser(999L, UserRole.PROVIDER);
        reservation.setProvider(unauthorizedProvider);
        UpdateReservationStatusDTO updateDTO = createSampleUpdateStatusDTO(ReservationStatus.IN_PROGRESS);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(providerId);
            when(userRepository.findById(providerId)).thenReturn(Optional.of(provider));
            when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

            assertThatThrownBy(() -> reservationService.changeStatusOfReservationByProvider(reservationId, updateDTO))
                    .isInstanceOf(UnauthorizedReservationAccessException.class)
                    .hasMessage("Vous n'êtes pas autorisé à accepter cette réservation");
        }
    }

    @Test
    @DisplayName("changeStatusOfReservationByProvider should update status to CLOSED when current status is IN_PROGRESS")
    void changeStatusOfReservationByProvider_shouldUpdateStatusToClosed_whenCurrentStatusIsInProgress() {
        Long reservationId = 1L;
        Long providerId = 2L;
        User provider = createSampleUser(providerId, UserRole.PROVIDER);
        Reservation reservation = createSampleReservation(reservationId, ReservationStatus.IN_PROGRESS);
        Reservation updatedReservation = createSampleReservation(reservationId, ReservationStatus.CLOSED);
        ReservationResponseDTO expectedDto = createSampleReservationResponseDTO(reservationId,
                ReservationStatus.CLOSED);
        UpdateReservationStatusDTO updateDTO = createSampleUpdateStatusDTO(ReservationStatus.CLOSED);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(providerId);
            when(userRepository.findById(providerId)).thenReturn(Optional.of(provider));
            when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
            when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);
            when(reservationMapper.toResponseDTO(updatedReservation)).thenReturn(expectedDto);

            ReservationResponseDTO result = reservationService.changeStatusOfReservationByProvider(reservationId,
                    updateDTO);

            assertThat(result).isEqualTo(expectedDto);
            verify(userRepository).findById(providerId);
            verify(reservationRepository).findById(reservationId);
            verify(reservationRepository).save(any(Reservation.class));
            verify(reservationMapper).toResponseDTO(updatedReservation);
        }
    }

    @Test
    @DisplayName("changeStatusOfReservationByProvider should throw exception when user is not a provider")
    void changeStatusOfReservationByProvider_shouldThrowException_whenUserIsNotProvider() {
        Long reservationId = 1L;
        Long userId = 1L;
        User client = createSampleUser(userId, UserRole.CLIENT);
        UpdateReservationStatusDTO updateDTO = createSampleUpdateStatusDTO(ReservationStatus.IN_PROGRESS);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(client));

            assertThatThrownBy(() -> reservationService.changeStatusOfReservationByProvider(reservationId, updateDTO))
                    .isInstanceOf(UnauthorizedReservationAccessException.class)
                    .hasMessage("Seuls les prestataires peuvent modifier le statut d'une réservation");
        }
    }

    @Test
    @DisplayName("createReservation should create reservation successfully")
    void createReservation_shouldCreateReservationSuccessfully() {
        Long currentUserId = 1L;
        Long clientId = 1L;
        Long providerId = 2L;
        Long serviceId = 1L;

        User client = createSampleUser(clientId, UserRole.CLIENT);
        User provider = createSampleUser(providerId, UserRole.PROVIDER);
        Service service = createSampleService(serviceId, provider);

        ReservationDTO createDTO = new ReservationDTO();
        createDTO.setClientId(clientId);
        createDTO.setProviderId(providerId);
        createDTO.setServiceId(serviceId);
        createDTO.setReservationDate(LocalDateTime.now().plusDays(1));
        createDTO.setStartDate(LocalDateTime.now().plusDays(1));
        createDTO.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));
        createDTO.setTotalPrice(BigDecimal.valueOf(100.0));

        Reservation savedReservation = createSampleReservation(1L, ReservationStatus.PENDING);
        ReservationResponseDTO expectedDto = createSampleReservationResponseDTO(1L, ReservationStatus.PENDING);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
            when(userRepository.findById(providerId)).thenReturn(Optional.of(provider));
            when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
            when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);
            when(reservationMapper.toResponseDTO(savedReservation)).thenReturn(expectedDto);

            ReservationResponseDTO result = reservationService.createReservation(createDTO);

            assertThat(result).isEqualTo(expectedDto);
            verify(userRepository).findById(clientId);
            verify(userRepository).findById(providerId);
            verify(serviceRepository).findById(serviceId);
            verify(reservationRepository).save(any(Reservation.class));
            verify(reservationMapper).toResponseDTO(savedReservation);
        }
    }

    @Test
    @DisplayName("createReservation should throw exception when user is not the client")
    void createReservation_shouldThrowException_whenUserIsNotClient() {
        Long currentUserId = 1L;
        Long clientId = 2L;

        ReservationDTO createDTO = new ReservationDTO();
        createDTO.setClientId(clientId);
        createDTO.setProviderId(3L);
        createDTO.setServiceId(1L);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);

            assertThatThrownBy(() -> reservationService.createReservation(createDTO))
                    .isInstanceOf(UnauthorizedReservationAccessException.class)
                    .hasMessage("Vous n'êtes pas autorisé à créer cette réservation");
        }
    }

    private User createSampleUser(Long id, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUserRole(role);
        user.setEmail("test@example.com");
        user.setPhoneNumber("123456789");
        user.setAddress("Test Address");
        user.setCity("Test City");
        user.setPostalCode("12345");

        return user;
    }

    private Service createSampleService(Long id, User provider) {
        Service service = new Service();
        service.setId(id);
        service.setTitle("Test Service");
        service.setDescription("Test Description");
        service.setPrice(BigDecimal.valueOf(100.0));
        service.setProvider(provider);

        return service;
    }

    private Reservation createSampleReservation(Long id, ReservationStatus status) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setStatus(status);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStartDate(LocalTime.of(10, 0));
        reservation.setEndDate(LocalTime.of(12, 0));
        reservation.setTotalPrice(BigDecimal.valueOf(100.0));

        User client = new User();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUserRole(UserRole.CLIENT);
        reservation.setClient(client);

        User provider = new User();
        provider.setId(2L);
        provider.setFirstName("Jane");
        provider.setLastName("Smith");
        provider.setUserRole(UserRole.PROVIDER);
        reservation.setProvider(provider);

        Service service = new Service();
        service.setId(1L);
        service.setTitle("Test Service");
        service.setDescription("Test Description");
        service.setPrice(BigDecimal.valueOf(100.0));
        service.setProvider(provider);
        reservation.setService(service);

        return reservation;
    }

    private ReservationResponseDTO createSampleReservationResponseDTO(Long id, ReservationStatus status) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(id);
        dto.setStatus(status);
        dto.setReservationDate(LocalDateTime.now());
        dto.setReservationDate(LocalDateTime.now().plusDays(1));
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));
        dto.setTotalPrice(BigDecimal.valueOf(100.0));

        return dto;
    }

    private UpdateReservationStatusDTO createSampleUpdateStatusDTO(ReservationStatus status) {
        UpdateReservationStatusDTO dto = new UpdateReservationStatusDTO();
        dto.setStatus(status);
        return dto;
    }
}