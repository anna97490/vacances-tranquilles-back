package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.StripeCheckoutSessionRequestDTO;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.exception.StripeSessionCreationException;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripeServiceImplTest {

    @Mock
    private ServiceRepository serviceRepo;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private StripeServiceImpl stripeService;

    @Test
    void createCheckoutSession_shouldReturnSessionId() {
        // Arrange
        StripeCheckoutSessionRequestDTO dto = new StripeCheckoutSessionRequestDTO();
        dto.setServiceId(1L);
        dto.setCustomerId(2L);
        dto.setProviderId(3L);
        dto.setDate(LocalDate.of(2025, 8, 8));
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(12, 0));

        Service service = new Service();
        service.setId(1L);
        service.setTitle("Massage");
        service.setPrice(BigDecimal.valueOf(50.0));

        when(serviceRepo.findById(1L)).thenReturn(Optional.of(service));

        Session mockSession = mock(Session.class);
        when(mockSession.getId()).thenReturn("sess_12345");

        // Mock static method
        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            mockedSession.when(() -> Session.create(any(SessionCreateParams.class)))
                    .thenReturn(mockSession);

            // Act
            Map<String, String> result = stripeService.createCheckoutSession(dto);

            // Assert
            assertThat(result).containsEntry("sessionId", "sess_12345");
        }
    }

    @Test
    void createCheckoutSession_shouldThrowException_ifStripeFails() {
        // Arrange
        StripeCheckoutSessionRequestDTO dto = new StripeCheckoutSessionRequestDTO();
        dto.setServiceId(1L);
        dto.setCustomerId(2L);
        dto.setProviderId(3L);
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(12, 0));
    
        Service service = new Service();
        service.setId(1L);
        service.setTitle("Massage");
        service.setPrice(BigDecimal.valueOf(50.0));
        when(serviceRepo.findById(1L)).thenReturn(Optional.of(service));
    
        // On moque Stripe.Session.create pour lever une StripeException
        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            StripeException stripeException = mock(StripeException.class);
            mockedSession
               .when(() -> Session.create(any(SessionCreateParams.class)))
               .thenThrow(stripeException);
    
            // Act & Assert
            assertThatThrownBy(() -> stripeService.createCheckoutSession(dto))
                .isInstanceOf(StripeSessionCreationException.class)
                .hasMessageContaining("La création de la session Stripe a échoué");
        }
    }

    @Test
    void confirmReservation_shouldCallReservationService() {
        // Arrange
        Session session = new Session();
        Map<String, String> metadata = Map.of(
                "serviceId", "1",
                "customerId", "2",
                "providerId", "3",
                "date", "2025-08-08",
                "startTime", "10:00",
                "endTime", "12:00");

        Service service = new Service();
        service.setId(1L);
        service.setPrice(BigDecimal.valueOf(50.0));

        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            mockedSession.when(() -> Session.retrieve("sess_123"))
                    .thenReturn(session);
            session.setMetadata(metadata);

            when(serviceRepo.findById(1L)).thenReturn(Optional.of(service));

            // Act
            stripeService.confirmReservation("sess_123");

            // Assert
            verify(reservationService, times(1)).createReservation(any(ReservationDTO.class));
        }
    }

    @Test
    void confirmReservation_shouldThrowException_whenStripeFails() {
        // On moque Stripe.Session.retrieve pour lever une StripeException
        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            StripeException stripeException = mock(StripeException.class);
            mockedSession
               .when(() -> Session.retrieve("invalid_id"))
               .thenThrow(stripeException);
    
            // Act & Assert
            assertThatThrownBy(() -> stripeService.confirmReservation("invalid_id"))
                .isInstanceOf(StripeSessionCreationException.class)
                .hasMessageContaining("La récupération de la session Stripe a échoué");
        }
    }
}
