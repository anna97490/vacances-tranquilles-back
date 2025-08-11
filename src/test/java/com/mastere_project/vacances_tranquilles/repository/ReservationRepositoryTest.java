package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.model.enums.ReservationStatus;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests pour ReservationRepository.
 * Utilise @DataJpaTest pour tester les opérations JPA sans démarrer le serveur complet.
 */
@DataJpaTest
@ActiveProfiles("test")
class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    private User client;
    private User provider;
    private Service service;
    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;

    @BeforeEach
    void setUp() {
        // Créer un client
        client = new User();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPassword("password123");
        client.setUserRole(UserRole.CLIENT);
        client.setPhoneNumber("0123456789");
        client.setAddress("123 Client Street");
        client.setCity("Client City");
        client.setPostalCode("12345");
        entityManager.persistAndFlush(client);

        // Créer un prestataire
        provider = new User();
        provider.setFirstName("Jane");
        provider.setLastName("Smith");
        provider.setEmail("jane.smith@example.com");
        provider.setPassword("password123");
        provider.setUserRole(UserRole.PROVIDER);
        provider.setPhoneNumber("0987654321");
        provider.setAddress("456 Provider Street");
        provider.setCity("Provider City");
        provider.setPostalCode("54321");
        provider.setCompanyName("Test Company");
        provider.setSiretSiren("12345678900000");
        entityManager.persistAndFlush(provider);

        // Créer un service
        service = new Service();
        service.setTitle("Test Service");
        service.setDescription("Test Description");
        service.setCategory("Test Category");
        service.setPrice(BigDecimal.valueOf(100.0));
        service.setProvider(provider);
        entityManager.persistAndFlush(service);

        // Créer des réservations de test
        reservation1 = createReservation(client, provider, service, ReservationStatus.PENDING);
        reservation2 = createReservation(client, provider, service, ReservationStatus.IN_PROGRESS);
        reservation3 = createReservation(client, provider, service, ReservationStatus.CLOSED);

        entityManager.persistAndFlush(reservation1);
        entityManager.persistAndFlush(reservation2);
        entityManager.persistAndFlush(reservation3);
    }

    @Test
    void findByClientId_ShouldReturnClientReservations() {
        // Act
        List<Reservation> clientReservations = reservationRepository.findByClientId(client.getId());

        // Assert
        assertThat(clientReservations)
                .hasSize(3)
                .allMatch(r -> r.getClient().getId().equals(client.getId()));
    }

    @Test
    void findByProviderId_ShouldReturnProviderReservations() {
        // Act
        List<Reservation> providerReservations = reservationRepository.findByProviderId(provider.getId());

        // Assert
        assertThat(providerReservations)
                .hasSize(3)
                .allMatch(r -> r.getProvider().getId().equals(provider.getId()));
    }

    @Test
    void findByClientIdOrProviderId_ShouldReturnAllUserReservations() {
        // Act
        List<Reservation> userReservations = reservationRepository.findByClientIdOrProviderId(client.getId(), client.getId());

        // Assert
        assertThat(userReservations)
                .hasSize(3)
                .allMatch(r -> 
                    r.getClient().getId().equals(client.getId()) || r.getProvider().getId().equals(client.getId()));
    }

    @Test
    void findByStatusAndClientId_ShouldReturnFilteredReservations() {
        // Act
        List<Reservation> pendingReservations = reservationRepository.findByStatusAndClientId(ReservationStatus.PENDING, client.getId());

        // Assert
        assertThat(pendingReservations)
                .hasSize(1)
                .first()
                .satisfies(reservation -> {
                    assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
                    assertThat(reservation.getClient().getId()).isEqualTo(client.getId());
                });
    }

    @Test
    void findByStatusAndProviderId_ShouldReturnFilteredReservations() {
        // Act
        List<Reservation> inProgressReservations = reservationRepository.findByStatusAndProviderId(ReservationStatus.IN_PROGRESS, provider.getId());

        // Assert
        assertThat(inProgressReservations)
                .hasSize(1)
                .first()
                .satisfies(reservation -> {
                    assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
                    assertThat(reservation.getProvider().getId()).isEqualTo(provider.getId());
                });
    }

    @Test
    void findByStatus_ShouldReturnAllReservationsWithStatus() {
        // Act
        List<Reservation> closedReservations = reservationRepository.findByStatus(ReservationStatus.CLOSED);

        // Assert
        assertThat(closedReservations)
                .hasSize(1)
                .first()
                .satisfies(reservation -> assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CLOSED));
    }

    @Test
    void findByIdAndUserId_WhenUserIsClient_ShouldReturnReservation() {
        // Act
        Optional<Reservation> foundReservation = reservationRepository.findByIdAndUserId(reservation1.getId(), client.getId());

        // Assert
        assertThat(foundReservation)
                .isPresent()
                .get()
                .satisfies(reservation -> {
                    assertThat(reservation.getId()).isEqualTo(reservation1.getId());
                    assertThat(reservation.getClient().getId()).isEqualTo(client.getId());
                });
    }

    @Test
    void findByIdAndUserId_WhenUserIsProvider_ShouldReturnReservation() {
        // Act
        Optional<Reservation> foundReservation = reservationRepository.findByIdAndUserId(reservation1.getId(), provider.getId());

        // Assert
        assertThat(foundReservation)
                .isPresent()
                .get()
                .satisfies(reservation -> {
                    assertThat(reservation.getId()).isEqualTo(reservation1.getId());
                    assertThat(reservation.getProvider().getId()).isEqualTo(provider.getId());
                });
    }

    @Test
    void findByIdAndUserId_WhenUserNotInvolved_ShouldReturnEmpty() {
        // Arrange
        User otherUser = new User();
        otherUser.setFirstName("Other");
        otherUser.setLastName("User");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password123");
        otherUser.setUserRole(UserRole.CLIENT);
        otherUser.setPhoneNumber("0123456789");
        otherUser.setAddress("123 Other Street");
        otherUser.setCity("Other City");
        otherUser.setPostalCode("12345");
        entityManager.persistAndFlush(otherUser);

        // Act
        Optional<Reservation> foundReservation = reservationRepository.findByIdAndUserId(reservation1.getId(), otherUser.getId());

        // Assert
        assertThat(foundReservation).isEmpty();
    }

    @Test
    void findByIdAndUserId_WhenReservationDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Reservation> foundReservation = reservationRepository.findByIdAndUserId(999L, client.getId());

        // Assert
        assertThat(foundReservation).isEmpty();
    }

    @Test
    void save_ShouldPersistReservation() {
        // Arrange
        Reservation newReservation = createReservation(client, provider, service, ReservationStatus.PENDING);

        // Act
        Reservation savedReservation = reservationRepository.save(newReservation);

        // Assert
        assertThat(savedReservation)
                .satisfies(reservation -> {
                    assertThat(reservation.getId()).isNotNull();
                    assertThat(reservation.getClient().getId()).isEqualTo(client.getId());
                    assertThat(reservation.getProvider().getId()).isEqualTo(provider.getId());
                    assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
                });
    }

    @Test
    void findAll_ShouldReturnAllReservations() {
        // Act
        List<Reservation> allReservations = reservationRepository.findAll();

        // Assert
        assertThat(allReservations).hasSize(3);
    }

    @Test
    void deleteById_ShouldRemoveReservation() {
        // Act
        reservationRepository.deleteById(reservation1.getId());

        // Assert
        Optional<Reservation> foundReservation = reservationRepository.findById(reservation1.getId());
        assertThat(foundReservation).isEmpty();
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        // Act
        long count = reservationRepository.count();

        // Assert
        assertThat(count).isEqualTo(3);
    }

    /**
     * Crée une réservation de test avec les paramètres spécifiés.
     */
    private Reservation createReservation(User client, User provider, Service service, ReservationStatus status) {
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setProvider(provider);
        reservation.setService(service);
        reservation.setReservationDate(LocalDate.of(2024, 1, 15));
        reservation.setStartDate(LocalTime.of(10, 0));
        reservation.setEndDate(LocalTime.of(12, 0));
        reservation.setTotalPrice(BigDecimal.valueOf(100.0));
        reservation.setStatus(status);
        return reservation;
    }
}
