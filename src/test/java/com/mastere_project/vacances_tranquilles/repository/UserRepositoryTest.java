package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests pour UserRepository.
 * Utilise @DataJpaTest pour tester les opérations JPA sans démarrer le serveur complet.
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User testProvider;

    @BeforeEach
    void setUp() {
        // Créer un utilisateur de test
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setUserRole(UserRole.CLIENT);
        testUser.setPhoneNumber("0123456789");
        testUser.setAddress("123 Test Street");
        testUser.setCity("Test City");
        testUser.setPostalCode("12345");
        testUser.setCreatedAt(LocalDateTime.now());

        // Créer un prestataire de test
        testProvider = new User();
        testProvider.setFirstName("Provider");
        testProvider.setLastName("Test");
        testProvider.setEmail("provider@example.com");
        testProvider.setPassword("password123");
        testProvider.setUserRole(UserRole.PROVIDER);
        testProvider.setPhoneNumber("0987654321");
        testProvider.setAddress("456 Provider Street");
        testProvider.setCity("Provider City");
        testProvider.setPostalCode("54321");
        testProvider.setCompanyName("Test Company");
        testProvider.setSiretSiren("12345678900000");
        testProvider.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void save_ShouldPersistUser() {
        // Act
        User savedUser = userRepository.save(testUser);

        // Assert
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getUserRole()).isEqualTo(UserRole.CLIENT);
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<User> foundUser = userRepository.findById(999L);

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("Test");
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    void existsByEmail_WhenUserExists_ShouldReturnTrue() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(testProvider);

        // Act
        List<User> allUsers = userRepository.findAll();

        // Assert
        assertThat(allUsers).hasSize(2);
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        userRepository.deleteById(savedUser.getId());

        // Assert
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isEmpty();
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        // Arrange
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(testProvider);

        // Act
        long count = userRepository.count();

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    void save_WithProvider_ShouldPersistProviderData() {
        // Act
        User savedProvider = userRepository.save(testProvider);

        // Assert
        assertThat(savedProvider.getId()).isNotNull();
        assertThat(savedProvider.getCompanyName()).isEqualTo("Test Company");
        assertThat(savedProvider.getSiretSiren()).isEqualTo("12345678900000");
        assertThat(savedProvider.getUserRole()).isEqualTo(UserRole.PROVIDER);
    }
} 