package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.Payment;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class ReservationMapperImplTest {
    private final ReservationMapperImpl mapper = new ReservationMapperImpl();

    @Test
    @DisplayName("toDTO should map all basic reservation fields")
    void toDTO_shouldMapBasicFields() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservationDate(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservation.setStartDate(LocalDateTime.of(2024, 2, 1, 14, 0));
        reservation.setEndDate(LocalDateTime.of(2024, 2, 1, 18, 0));
        reservation.setComment("Test reservation");
        reservation.setTotalPrice(150.0);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(ReservationStatus.PENDING);
        assertThat(dto.getReservationDate()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 0));
        assertThat(dto.getStartDate()).isEqualTo(LocalDateTime.of(2024, 2, 1, 14, 0));
        assertThat(dto.getEndDate()).isEqualTo(LocalDateTime.of(2024, 2, 1, 18, 0));
        assertThat(dto.getComment()).isEqualTo("Test reservation");
        assertThat(dto.getTotalPrice()).isEqualTo(150.0);
    }

    @Test
    @DisplayName("toDTO should map client information")
    void toDTO_shouldMapClient() {
        // Arrange
        Reservation reservation = new Reservation();
        User client = new User();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUserRole(UserRole.CLIENT);
        client.setAddress("123 Main St");
        client.setCity("Paris");
        client.setPostalCode("75001");
        reservation.setClient(client);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getClient()).isNotNull();
        assertThat(dto.getClient().getId()).isEqualTo(1L);
        assertThat(dto.getClient().getFirstName()).isEqualTo("John");
        assertThat(dto.getClient().getLastName()).isEqualTo("Doe");
        assertThat(dto.getClient().getUserRole()).isEqualTo("CLIENT");
        assertThat(dto.getClient().getAddress()).isEqualTo("123 Main St");
        assertThat(dto.getClient().getCity()).isEqualTo("Paris");
        assertThat(dto.getClient().getPostalCode()).isEqualTo("75001");
    }

    @Test
    @DisplayName("toDTO should map provider information")
    void toDTO_shouldMapProvider() {
        // Arrange
        Reservation reservation = new Reservation();
        User provider = new User();
        provider.setId(2L);
        provider.setFirstName("Jane");
        provider.setLastName("Smith");
        provider.setUserRole(UserRole.PROVIDER);
        provider.setAddress("456 Oak Ave");
        provider.setCity("Lyon");
        provider.setPostalCode("69001");
        reservation.setProvider(provider);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getProvider()).isNotNull();
        assertThat(dto.getProvider().getId()).isEqualTo(2L);
        assertThat(dto.getProvider().getFirstName()).isEqualTo("Jane");
        assertThat(dto.getProvider().getLastName()).isEqualTo("Smith");
        assertThat(dto.getProvider().getUserRole()).isEqualTo("PROVIDER");
        assertThat(dto.getProvider().getAddress()).isEqualTo("456 Oak Ave");
        assertThat(dto.getProvider().getCity()).isEqualTo("Lyon");
        assertThat(dto.getProvider().getPostalCode()).isEqualTo("69001");
    }

    @Test
    @DisplayName("toDTO should map service information")
    void toDTO_shouldMapService() {
        // Arrange
        Reservation reservation = new Reservation();
        Service service = new Service();
        service.setId(1L);
        service.setTitle("Service Test");
        service.setDescription("Description du service");
        service.setPrice(100.0);
        
        User serviceProvider = new User();
        serviceProvider.setId(2L);
        service.setProvider(serviceProvider);
        
        reservation.setService(service);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getService()).isNotNull();
        assertThat(dto.getService().getId()).isEqualTo(1L);
        assertThat(dto.getService().getTitle()).isEqualTo("Service Test");
        assertThat(dto.getService().getDescription()).isEqualTo("Description du service");
        assertThat(dto.getService().getPrice()).isEqualTo(100.0);
        assertThat(dto.getService().getProviderId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("toDTO should map payment information")
    void toDTO_shouldMapPayment() {
        // Arrange
        Reservation reservation = new Reservation();
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(150.0);
        payment.setPaymentDate(LocalDateTime.of(2024, 1, 15, 10, 30));
        payment.setPaymentMethod("CARD");
        payment.setStatus("PAID");
        reservation.setPayment(payment);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getPayment()).isNotNull();
        assertThat(dto.getPayment().getId()).isEqualTo(1L);
        assertThat(dto.getPayment().getAmount()).isEqualTo(150.0);
        assertThat(dto.getPayment().getMethod()).isEqualTo("CARD");
        assertThat(dto.getPayment().getStatus()).isEqualTo("PAID");
        
        // Vérifier la conversion de LocalDateTime vers Date
        assertThat(dto.getPayment().getPaymentDate()).isInstanceOf(Date.class);
    }

    @Test
    @DisplayName("toDTO should handle null client")
    void toDTO_shouldHandleNullClient() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setClient(null);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getClient()).isNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toDTO should handle null provider")
    void toDTO_shouldHandleNullProvider() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setProvider(null);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getProvider()).isNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toDTO should handle null service")
    void toDTO_shouldHandleNullService() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setService(null);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getService()).isNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toDTO should handle null payment")
    void toDTO_shouldHandleNullPayment() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setPayment(null);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getPayment()).isNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toDTO should handle service with null provider")
    void toDTO_shouldHandleServiceWithNullProvider() {
        // Arrange
        Reservation reservation = new Reservation();
        Service service = new Service();
        service.setId(1L);
        service.setTitle("Service Test");
        service.setProvider(null);
        reservation.setService(service);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getService()).isNotNull();
        assertThat(dto.getService().getId()).isEqualTo(1L);
        assertThat(dto.getService().getTitle()).isEqualTo("Service Test");
        assertThat(dto.getService().getProviderId()).isNull();
    }

    @Test
    @DisplayName("toDTO should handle payment with null payment date")
    void toDTO_shouldHandlePaymentWithNullPaymentDate() {
        // Arrange
        Reservation reservation = new Reservation();
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(150.0);
        payment.setPaymentDate(null);
        payment.setPaymentMethod("CARD");
        payment.setStatus("PAID");
        reservation.setPayment(payment);

        // Act
        ReservationDTO dto = mapper.toDTO(reservation);

        // Assert
        assertThat(dto.getPayment()).isNotNull();
        assertThat(dto.getPayment().getId()).isEqualTo(1L);
        assertThat(dto.getPayment().getAmount()).isEqualTo(150.0);
        assertThat(dto.getPayment().getPaymentDate()).isNull();
    }
} 