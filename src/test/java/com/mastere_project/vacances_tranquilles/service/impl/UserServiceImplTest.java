package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.exception.EmailAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.MissingFieldException;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Doit enregistrer un client correctement")
    void registerClientSuccessfully() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Martin");
        dto.setEmail("alice.martin@example.com");
        dto.setPhoneNumber("0612345678");
        dto.setAddress("12 rue de Paris");
        dto.setPostalCode("75001");
        dto.setCity("Paris");

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setPostalCode(dto.getPostalCode());
        user.setCity(dto.getCity());
        user.setUserRole(UserRole.CLIENT);

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userMapper.toUser(dto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerClient(dto);

        verify(userRepository).existsByEmail(dto.getEmail());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userMapper).toUser(dto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Doit lever une exception si l'email du client existe déjà")
    void registerClientThrowsExceptionIfEmailAlreadyExists() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setEmail("alice.martin@example.com");
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerClient(dto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Un compte avec cet email existe déjà.");
        verify(userRepository).existsByEmail(dto.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder, userMapper);
    }

    @Test
    @DisplayName("Doit enregistrer un prestataire correctement")
    void registerProviderSuccessfully() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setFirstName("Bob");
        dto.setLastName("Durand");
        dto.setEmail("bob.durand@example.com");
        dto.setPhoneNumber("0699999999");
        dto.setAddress("99 avenue de Lyon");
        dto.setPostalCode("69000");
        dto.setCity("Lyon");
        dto.setCompanyName("Bâtiment Services");
        dto.setSiretSiren("12345678901234");

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setPostalCode(dto.getPostalCode());
        user.setCity(dto.getCity());
        user.setUserRole(UserRole.PRESTATAIRE);
        user.setCompanyName(dto.getCompanyName());
        user.setSiretSiren(dto.getSiretSiren());

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword2");
        when(userMapper.toUser(dto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerProvider(dto);

        verify(userRepository).existsByEmail(dto.getEmail());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userMapper).toUser(dto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Doit lever une exception si l'email du prestataire existe déjà")
    void registerProviderThrowsExceptionIfEmailAlreadyExists() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("bob.durand@example.com");
        dto.setFirstName("Bob");
        dto.setLastName("Durand");
        dto.setPassword("motdepasse");
        dto.setPhoneNumber("0607080910");
        dto.setAddress("1 rue du Test");
        dto.setCity("Lyon");
        dto.setPostalCode("69000");
        dto.setCompanyName("TestSociete");
        dto.setSiretSiren("12345678901234");
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerProvider(dto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Un compte avec cet email existe déjà.");
        verify(userRepository).existsByEmail(dto.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder, userMapper);
    }

    @Test
    @DisplayName("Doit lever une exception si les infos SIRET/SIREN ou compagnie du prestataire sont manquantes")
    void registerProviderThrowsExceptionIfCompanyNameOrSirenMissing() {
        // Préparation
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("new.provider@example.com");
        dto.setFirstName("Bob");
        dto.setLastName("Durand");
        dto.setPassword("motdepasse");
        dto.setPhoneNumber("0607080910");
        dto.setAddress("1 rue du Test");
        dto.setCity("Lyon");
        dto.setPostalCode("69000");
        dto.setCompanyName(""); // <- champ vide
        dto.setSiretSiren(null); // <- champ null

        // Action & Vérification
        assertThatThrownBy(() -> userService.registerProvider(dto))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageContaining("Le nom de la société est obligatoire pour les prestataires");


        // Ici, PAS de vérification sur le repository
        verifyNoInteractions(userRepository, passwordEncoder, userMapper);
    }
}