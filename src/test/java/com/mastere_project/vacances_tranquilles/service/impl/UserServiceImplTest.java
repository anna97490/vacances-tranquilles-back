package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.LoginResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.dto.UserDTO;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.EmailAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.EmailNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.MissingFieldException;
import com.mastere_project.vacances_tranquilles.exception.WrongPasswordException;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// ... imports et début de classe déjà présents ...

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("registerClient - should save user when email does not exist")
    void registerClient_shouldSaveUser_whenEmailNotExists() {
        RegisterClientDTO dto = mock(RegisterClientDTO.class);
        User user = mock(User.class);

        when(dto.getEmail()).thenReturn("test@example.com");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toUser(dto)).thenReturn(user);
        when(user.getPassword()).thenReturn("plainPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        userService.registerClient(dto);

        verify(user).setPassword("encodedPassword");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("registerClient - should throw if email exists")
    void registerClient_shouldThrow_whenEmailExists() {
        RegisterClientDTO dto = mock(RegisterClientDTO.class);
        when(dto.getEmail()).thenReturn("test@example.com");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerClient(dto))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("registerProvider - should throw if companyName is missing")
    void registerProvider_shouldThrow_whenCompanyNameMissing() {
        RegisterProviderDTO dto = mock(RegisterProviderDTO.class);
        when(dto.getCompanyName()).thenReturn(null);

        assertThatThrownBy(() -> userService.registerProvider(dto))
                .isInstanceOf(MissingFieldException.class);
    }

    @Test
    @DisplayName("registerProvider - should throw if siretSiren is missing")
    void registerProvider_shouldThrow_whenSiretSirenMissing() {
        RegisterProviderDTO dto = mock(RegisterProviderDTO.class);
        when(dto.getCompanyName()).thenReturn("Company");
        when(dto.getSiretSiren()).thenReturn(null);

        assertThatThrownBy(() -> userService.registerProvider(dto))
                .isInstanceOf(MissingFieldException.class);
    }

    @Test
    @DisplayName("registerProvider - should throw if email exists")
    void registerProvider_shouldThrow_whenEmailExists() {
        RegisterProviderDTO dto = mock(RegisterProviderDTO.class);
        when(dto.getCompanyName()).thenReturn("Company");
        when(dto.getSiretSiren()).thenReturn("123456789");
        when(dto.getEmail()).thenReturn("test@example.com");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerProvider(dto))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("registerProvider - should save user when all fields are valid and email not exists")
    void registerProvider_shouldSaveUser_whenValid() {
        RegisterProviderDTO dto = mock(RegisterProviderDTO.class);
        User user = mock(User.class);

        when(dto.getCompanyName()).thenReturn("Company");
        when(dto.getSiretSiren()).thenReturn("123456789");
        when(dto.getEmail()).thenReturn("provider@example.com");
        when(userRepository.existsByEmail("provider@example.com")).thenReturn(false);
        when(userMapper.toUser(dto)).thenReturn(user);
        when(user.getPassword()).thenReturn("plainPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        userService.registerProvider(dto);

        verify(user).setPassword("encodedPassword");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("login - should throw if email not found")
    void login_shouldThrow_whenEmailNotFound() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getEmail()).thenReturn("notfound@example.com");
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(userDTO))
                .isInstanceOf(EmailNotFoundException.class);
    }

    @Test
    @DisplayName("login - should throw if password is wrong")
    void login_shouldThrow_whenPasswordWrong() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getEmail()).thenReturn("user@example.com");
        when(userDTO.getPassword()).thenReturn("wrongpass");

        User user = new User();
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "encodedPassword")).thenReturn(false);

        assertThatThrownBy(() -> userService.login(userDTO))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    @DisplayName("login - should return token and role on success")
    void login_shouldReturnTokenAndRole_onSuccess() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getEmail()).thenReturn("user@example.com");
        when(userDTO.getPassword()).thenReturn("goodpass");

        User user = new User();
        user.setPassword("encodedPassword");
        user.setEmail("user@example.com");
        user.setUserRole(UserRole.CLIENT);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("goodpass", "encodedPassword")).thenReturn(true);
        when(jwtConfig.generateToken("user@example.com", UserRole.CLIENT)).thenReturn("token");

        LoginResponseDTO response = userService.login(userDTO);

        assertThat(response.getToken()).isEqualTo("token");
        assertThat(response.getUserRole()).isEqualTo(UserRole.CLIENT);
    }

    @Test
    @DisplayName("login - should throw RuntimeException on unexpected error")
    void login_shouldThrow_onUnexpectedException() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getEmail()).thenReturn("user@example.com");
        // Simuler une exception inattendue dans findByEmail
        when(userRepository.findByEmail("user@example.com")).thenThrow(new RuntimeException("DB down"));

        assertThatThrownBy(() -> userService.login(userDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Erreur serveur inattendue");
    }

    @Test
    @DisplayName("login - should throw if user is blocked")
    void login_shouldThrow_whenUserIsBlocked() throws Exception {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getEmail()).thenReturn("blocked@example.com");

        // Simuler un blocage actif
        Field blockedUntilField = UserServiceImpl.class.getDeclaredField("blockedUntil");
        blockedUntilField.setAccessible(true);
        Map<String, Long> blockedMap = new ConcurrentHashMap<>();
        blockedMap.put("blocked@example.com", System.currentTimeMillis() + 10000);
        blockedUntilField.set(userService, blockedMap);

        assertThatThrownBy(() -> userService.login(userDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Trop de tentatives échouées");
    }

    @Test
    @DisplayName("incrementLoginAttempts - should increment but not block if under max")
    void incrementLoginAttempts_shouldIncrementButNotBlock() throws Exception {
        Field loginAttemptsField = UserServiceImpl.class.getDeclaredField("loginAttempts");
        loginAttemptsField.setAccessible(true);
        Map<String, Integer> attempts = new ConcurrentHashMap<>();
        attempts.put("test2@example.com", 2);
        loginAttemptsField.set(userService, attempts);

        Field blockedUntilField = UserServiceImpl.class.getDeclaredField("blockedUntil");
        blockedUntilField.setAccessible(true);
        Map<String, Long> blockedMap = new ConcurrentHashMap<>();
        blockedUntilField.set(userService, blockedMap);

        Method method = UserServiceImpl.class.getDeclaredMethod("incrementLoginAttempts", String.class);
        method.setAccessible(true);
        method.invoke(userService, "test2@example.com");

        // Vérifier que l'utilisateur n'est pas bloqué
        blockedMap = (Map<String, Long>) blockedUntilField.get(userService);
        assertThat(blockedMap.get("test2@example.com")).isNull();
        // Et que le compteur a bien augmenté
        attempts = (Map<String, Integer>) loginAttemptsField.get(userService);
        assertThat(attempts.get("test2@example.com")).isEqualTo(3);
    }

    @Test
    @DisplayName("login - should reset counters after successful login")
    void login_shouldResetCountersAfterSuccess() throws Exception {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getEmail()).thenReturn("reset@example.com");
        when(userDTO.getPassword()).thenReturn("goodpass");

        User user = new User();
        user.setPassword("encodedPassword");
        user.setEmail("reset@example.com");
        user.setUserRole(UserRole.CLIENT);

        when(userRepository.findByEmail("reset@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("goodpass", "encodedPassword")).thenReturn(true);
        when(jwtConfig.generateToken("reset@example.com", UserRole.CLIENT)).thenReturn("token");

        // Simuler des compteurs existants
        Field loginAttemptsField = UserServiceImpl.class.getDeclaredField("loginAttempts");
        loginAttemptsField.setAccessible(true);
        Map<String, Integer> attempts = new ConcurrentHashMap<>();
        attempts.put("reset@example.com", 2);
        loginAttemptsField.set(userService, attempts);

        Field blockedUntilField = UserServiceImpl.class.getDeclaredField("blockedUntil");
        blockedUntilField.setAccessible(true);
        Map<String, Long> blockedMap = new ConcurrentHashMap<>();
        blockedUntilField.set(userService, blockedMap);

        userService.login(userDTO);

        // Les compteurs doivent être reset
        attempts = (Map<String, Integer>) loginAttemptsField.get(userService);
        blockedMap = (Map<String, Long>) blockedUntilField.get(userService);
        assertThat(attempts.get("reset@example.com")).isNull();
        assertThat(blockedMap.get("reset@example.com")).isNull();
    }

    @Test
    @DisplayName("incrementLoginAttempts - should block user after max attempts")
    void incrementLoginAttempts_shouldBlockUser() throws Exception {
        Field loginAttemptsField = UserServiceImpl.class.getDeclaredField("loginAttempts");
        loginAttemptsField.setAccessible(true);
        Map<String, Integer> attempts = new ConcurrentHashMap<>();
        attempts.put("block@example.com", 4); // MAX_ATTEMPTS = 5
        loginAttemptsField.set(userService, attempts);

        Field blockedUntilField = UserServiceImpl.class.getDeclaredField("blockedUntil");
        blockedUntilField.setAccessible(true);
        Map<String, Long> blockedMap = new ConcurrentHashMap<>();
        blockedUntilField.set(userService, blockedMap);

        Method method = UserServiceImpl.class.getDeclaredMethod("incrementLoginAttempts", String.class);
        method.setAccessible(true);
        method.invoke(userService, "block@example.com");

        // Vérifier que l'utilisateur est bloqué
        blockedMap = (Map<String, Long>) blockedUntilField.get(userService);
        assertThat(blockedMap.get("block@example.com")).isNotNull();
        // Et que le compteur a été supprimé
        attempts = (Map<String, Integer>) loginAttemptsField.get(userService);
        assertThat(attempts.get("block@example.com")).isNull();
    }
}
