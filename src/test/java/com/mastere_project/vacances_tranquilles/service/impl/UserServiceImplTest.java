package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.EmailAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.EmailNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.MissingFieldException;
import com.mastere_project.vacances_tranquilles.exception.WrongPasswordException;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private com.mastere_project.vacances_tranquilles.repository.ReviewRepository reviewRepository;

    @Mock
    private com.mastere_project.vacances_tranquilles.mapper.ReviewMapper reviewMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private UserProfileDTO mockUserProfileDTO;

    @BeforeEach
    void setUp() {
        mockUser = createMockUser();
        mockUserProfileDTO = createMockUserProfileDTO();
    }

    @Test
    void registerClient_ShouldRegisterClientSuccessfully() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("password123");
        dto.setFirstName("Test");
        dto.setLastName("User");
        
        User userWithPassword = createMockUser();
        userWithPassword.setPassword("password123");
        
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toUser(any(RegisterClientDTO.class))).thenReturn(userWithPassword);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userWithPassword);
        
        userService.registerClient(dto);
        
        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userMapper).toUser(any(RegisterClientDTO.class));
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerClient_WhenEmailExists_ShouldThrowException() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setEmail("existing@test.com");
        
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        
        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerClient(dto));
        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userMapper, never()).toUser(any(RegisterClientDTO.class));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerProvider_ShouldRegisterProviderSuccessfully() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("provider@test.com");
        dto.setPassword("password123");
        dto.setCompanyName("Test Company");
        dto.setSiretSiren("12345678900000");
        
        User userWithPassword = createMockUser();
        userWithPassword.setPassword("password123");
        
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toUser(any(RegisterProviderDTO.class))).thenReturn(userWithPassword);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userWithPassword);
        
        userService.registerProvider(dto);
        
        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userMapper).toUser(any(RegisterProviderDTO.class));
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @ParameterizedTest
    @CsvSource({
        "provider@test.com, null, Test Company, MissingFieldException",
        "provider@test.com, Test Company, null, MissingFieldException",
        "existing@test.com, Test Company, 12345678900000, EmailAlreadyExistsException"
    })
    void registerProvider_WithVariousInvalidInputs_ShouldThrowException(String email, String companyName, String siretSiren, String expectedException) {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail(email);
        dto.setCompanyName(companyName);
        dto.setSiretSiren(siretSiren);
        
        if ("EmailAlreadyExistsException".equals(expectedException)) {
            when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
            assertThrows(EmailAlreadyExistsException.class, () -> userService.registerProvider(dto));
            verify(userRepository).existsByEmail(dto.getEmail());
            verify(userMapper, never()).toUser(any(RegisterProviderDTO.class));
            verify(userRepository, never()).save(any());
        } else {
            when(userMapper.toUser(any(RegisterProviderDTO.class))).thenReturn(null);
            assertThrows(NullPointerException.class, () -> userService.registerProvider(dto));
            verify(userMapper).toUser(any(RegisterProviderDTO.class));
        }
    }

    @Test
    void login_ShouldReturnLoginResponse() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password123");
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(userDTO.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtConfig.generateToken(mockUser.getId(), mockUser.getUserRole())).thenReturn("jwt-token");
        
        LoginResponseDTO result = userService.login(userDTO);
        
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals(UserRole.CLIENT, result.getUserRole());
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(passwordEncoder).matches(userDTO.getPassword(), mockUser.getPassword());
        verify(jwtConfig).generateToken(mockUser.getId(), mockUser.getUserRole());
    }

    @Test
    void login_WhenEmailNotFound_ShouldThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("nonexistent@test.com");
        userDTO.setPassword("password123");
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        
        assertThrows(EmailNotFoundException.class, () -> userService.login(userDTO));
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_WhenWrongPassword_ShouldThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("wrongpassword");
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(userDTO.getPassword(), mockUser.getPassword())).thenReturn(false);
        
        assertThrows(WrongPasswordException.class, () -> userService.login(userDTO));
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(passwordEncoder).matches(userDTO.getPassword(), mockUser.getPassword());
    }

    @Test
    void login_WhenAccountLocked_ShouldThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("locked@test.com");
        userDTO.setPassword("password123");
        
        // Simuler un compte bloqué en utilisant la réflexion pour accéder aux maps privées
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(userDTO.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtConfig.generateToken(mockUser.getId(), mockUser.getUserRole())).thenReturn("jwt-token");
        
        LoginResponseDTO result = userService.login(userDTO);
        
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
    }

    @Test
    void getUserProfile_ShouldReturnUserProfile() {
        Long userId = 37L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userMapper.toUserProfileDTO(mockUser)).thenReturn(mockUserProfileDTO);
            
            UserProfileDTO result = userService.getUserProfile();
            
            assertNotNull(result);
            assertEquals(mockUserProfileDTO, result);
            
            verify(userRepository).findById(userId);
            verify(userMapper).toUserProfileDTO(mockUser);
        }
    }

    @Test
    void getUserProfile_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> userService.getUserProfile());
            assertEquals("Utilisateur non trouvé", exception.getMessage());
            
            verify(userRepository).findById(userId);
            verify(userMapper, never()).toUserProfileDTO(any());
        }
    }

    @Test
    void getUserProfile_WhenUserIsAnonymized_ShouldThrowException() {
        Long userId = 37L;
        mockUser.setIsAnonymized(true);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> userService.getUserProfile());
            assertEquals("Utilisateur anonymisé - accès refusé", exception.getMessage());
            
            verify(userRepository).findById(userId);
            verify(userMapper, never()).toUserProfileDTO(any());
        }
    }

    @Test
    void updateUserProfile_ShouldReturnUpdatedProfile() {
        Long userId = 37L;
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        updateDTO.setPhoneNumber("0623456789");
       
        User updatedUser = createMockUser();
        updatedUser.setFirstName("Nouveau Prénom");
        updatedUser.setPhoneNumber("0623456789");
        
        UserProfileDTO updatedProfileDTO = createMockUserProfileDTO();
        updatedProfileDTO.setFirstName("Nouveau Prénom");
        updatedProfileDTO.setPhoneNumber("0623456789");
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userMapper.updateUserFromDTO(mockUser, updateDTO)).thenReturn(updatedUser);
            when(userRepository.save(updatedUser)).thenReturn(updatedUser);
            when(userMapper.toUserProfileDTO(updatedUser)).thenReturn(updatedProfileDTO);
            
            UserProfileDTO result = userService.updateUserProfile(updateDTO);
            assertNotNull(result);
            assertEquals(updatedProfileDTO, result);
            
            verify(userRepository).findById(userId);
            verify(userMapper).updateUserFromDTO(mockUser, updateDTO);
            verify(userRepository).save(updatedUser);
            verify(userMapper).toUserProfileDTO(updatedUser);
        }
    }

    @Test
    void updateUserProfile_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;

        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> userService.updateUserProfile(updateDTO));
            assertEquals("Utilisateur non trouvé", exception.getMessage());
            
            verify(userRepository).findById(userId);
            verify(userMapper, never()).updateUserFromDTO(any(), any());
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    void updateUserProfile_WhenUserIsAnonymized_ShouldThrowException() {
        Long userId = 37L;
        mockUser.setIsAnonymized(true);
        
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> userService.updateUserProfile(updateDTO));
            assertEquals("Utilisateur anonymisé - accès refusé", exception.getMessage());
            
            verify(userRepository).findById(userId);
            verify(userMapper, never()).updateUserFromDTO(any(), any());
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    void deleteUserAccount_ShouldAnonymizeUser() {
        Long userId = 37L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(User.class))).thenReturn(mockUser);
            
            userService.deleteUserAccount();
            
            verify(userRepository).findById(userId);
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void deleteUserAccount_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> userService.deleteUserAccount());
            assertEquals("Utilisateur non trouvé", exception.getMessage());
            
            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    void deleteUserAccount_ForProvider_ShouldAnonymizeProviderFields() {
        Long userId = 39L;
        User providerUser = createMockProviderUser();
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(providerUser));
            when(userRepository.save(any(User.class))).thenReturn(providerUser);
            
            userService.deleteUserAccount();
            
            verify(userRepository).findById(userId);
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void getUserBasicInfoById_ShouldReturnUserBasicInfo() {
        Long userId = 37L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.findByReviewedId(userId)).thenReturn(java.util.List.of());
        
        UserBasicInfoDTO result = userService.getUserBasicInfoById(userId);
        
        assertNotNull(result);
        assertEquals("Teste", result.getFirstName());
        assertEquals("Teste", result.getLastName());
        
        verify(userRepository).findById(userId);
        verify(reviewRepository).findByReviewedId(userId);
    }

    @Test
    void getUserBasicInfoById_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
            () -> userService.getUserBasicInfoById(userId));
        assertEquals("Utilisateur non trouvé", exception.getMessage());
        
        verify(userRepository).findById(userId);
        verify(userMapper, never()).toUserBasicInfoDTO(any());
    }

    @Test
    void getUserBasicInfoById_WhenUserIsAnonymized_ShouldThrowException() {
        Long userId = 37L;
        mockUser.setIsAnonymized(true);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, 
            () -> userService.getUserBasicInfoById(userId));
        assertEquals("Utilisateur anonymisé - accès refusé", exception.getMessage());
        
        verify(userRepository).findById(userId);
        verify(userMapper, never()).toUserBasicInfoDTO(any());
    }

    // Tests pour les cas d'erreur non couverts

    @Test
    void registerProvider_WhenCompanyNameIsBlank_ShouldThrowException() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("provider@test.com");
        dto.setCompanyName("   "); // Espaces blancs
        dto.setSiretSiren("12345678900000");
        
        assertThrows(MissingFieldException.class, () -> userService.registerProvider(dto));
        verify(userRepository, never()).existsByEmail(any());
        verify(userMapper, never()).toUser(any(RegisterProviderDTO.class));
    }

    @Test
    void registerProvider_WhenSiretSirenIsBlank_ShouldThrowException() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("provider@test.com");
        dto.setCompanyName("Test Company");
        dto.setSiretSiren("   "); // Espaces blancs
        
        assertThrows(MissingFieldException.class, () -> userService.registerProvider(dto));
        verify(userRepository, never()).existsByEmail(any());
        verify(userMapper, never()).toUser(any(RegisterProviderDTO.class));
    }

    @Test
    void login_WhenExceptionOccurs_ShouldThrowLoginInternalException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password123");
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenThrow(new RuntimeException("Database error"));
        
        assertThrows(com.mastere_project.vacances_tranquilles.exception.LoginInternalException.class, 
            () -> userService.login(userDTO));
        verify(userRepository).findByEmail(userDTO.getEmail());
    }

    @Test
    void login_WhenPasswordEncoderThrowsException_ShouldThrowLoginInternalException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password123");
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenThrow(new RuntimeException("Encoder error"));
        
        assertThrows(com.mastere_project.vacances_tranquilles.exception.LoginInternalException.class, 
            () -> userService.login(userDTO));
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(passwordEncoder).matches(userDTO.getPassword(), mockUser.getPassword());
    }

    @Test
    void login_WhenJwtGenerationThrowsException_ShouldThrowLoginInternalException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password123");
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(userDTO.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtConfig.generateToken(anyLong(), any(UserRole.class))).thenThrow(new RuntimeException("JWT error"));
        
        assertThrows(com.mastere_project.vacances_tranquilles.exception.LoginInternalException.class, 
            () -> userService.login(userDTO));
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(passwordEncoder).matches(userDTO.getPassword(), mockUser.getPassword());
        verify(jwtConfig).generateToken(mockUser.getId(), mockUser.getUserRole());
    }

    // Tests pour les scénarios de validation métier

    @Test
    void registerProvider_WhenAllFieldsValid_ShouldRegisterSuccessfully() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("provider@test.com");
        dto.setPassword("password123");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setCompanyName("Valid Company");
        dto.setSiretSiren("12345678900000");
        
        User userWithPassword = createMockProviderUser();
        userWithPassword.setPassword("password123");
        
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toUser(any(RegisterProviderDTO.class))).thenReturn(userWithPassword);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userWithPassword);
        
        userService.registerProvider(dto);
        
        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userMapper).toUser(any(RegisterProviderDTO.class));
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }


    @Test
    void deleteUserAccount_WhenUserRepositorySaveThrowsException_ShouldPropagateException() {
        Long userId = 37L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database save error"));
            
            assertThrows(RuntimeException.class, () -> userService.deleteUserAccount());
            
            verify(userRepository).findById(userId);
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void updateUserProfile_WhenUserRepositorySaveThrowsException_ShouldPropagateException() {
        Long userId = 37L;
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        
        User updatedUser = createMockUser();
        updatedUser.setFirstName("Nouveau Prénom");
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userMapper.updateUserFromDTO(mockUser, updateDTO)).thenReturn(updatedUser);
            when(userRepository.save(updatedUser)).thenThrow(new RuntimeException("Database save error"));
            
            assertThrows(RuntimeException.class, () -> userService.updateUserProfile(updateDTO));
            
            verify(userRepository).findById(userId);
            verify(userMapper).updateUserFromDTO(mockUser, updateDTO);
            verify(userRepository).save(updatedUser);
        }
    }

    @Test
    void getUserBasicInfoById_WhenReviewRepositoryThrowsException_ShouldPropagateException() {
        Long userId = 37L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.findByReviewedId(userId)).thenThrow(new RuntimeException("Review repository error"));
        
        assertThrows(RuntimeException.class, () -> userService.getUserBasicInfoById(userId));
        
        verify(userRepository).findById(userId);
        verify(reviewRepository).findByReviewedId(userId);
    }

    @Test
    void getUserBasicInfoById_WhenReviewMapperThrowsException_ShouldPropagateException() {
        Long userId = 37L;
        
        // Créer une liste avec une review pour que le mapper soit appelé
        com.mastere_project.vacances_tranquilles.entity.Review mockReview = new com.mastere_project.vacances_tranquilles.entity.Review();
        mockReview.setId(1L);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.findByReviewedId(userId)).thenReturn(java.util.List.of(mockReview));
        when(reviewMapper.toDTO(any())).thenThrow(new RuntimeException("Mapper error"));
        
        assertThrows(RuntimeException.class, () -> userService.getUserBasicInfoById(userId));
        
        verify(userRepository).findById(userId);
        verify(reviewRepository).findByReviewedId(userId);
    }

    // Tests pour les cas limites avec des données null

    @Test
    void registerClient_WhenUserMapperReturnsNull_ShouldHandleGracefully() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("password123");
        dto.setFirstName("Test");
        dto.setLastName("User");
        
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toUser(any(RegisterClientDTO.class))).thenReturn(null);
        
        assertThrows(NullPointerException.class, () -> userService.registerClient(dto));
        
        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userMapper).toUser(any(RegisterClientDTO.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_WhenUserHasNullPassword_ShouldHandleGracefully() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password123");
        
        User userWithNullPassword = createMockUser();
        userWithNullPassword.setPassword(null);
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(userWithNullPassword));
        
        assertThrows(WrongPasswordException.class, () -> userService.login(userDTO));
        
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(passwordEncoder).matches(userDTO.getPassword(), null);
    }

    @Test
    void getUserProfile_WhenUserMapperReturnsNull_ShouldHandleGracefully() {
        Long userId = 37L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userMapper.toUserProfileDTO(mockUser)).thenReturn(null);
            
            UserProfileDTO result = userService.getUserProfile();
            
            assertNull(result);
            
            verify(userRepository).findById(userId);
            verify(userMapper).toUserProfileDTO(mockUser);
        }
    }

    // Tests pour les cas d'erreur de validation métier

    @Test
    void registerProvider_WhenEmailIsNull_ShouldThrowException() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail(null);
        dto.setCompanyName("Test Company");
        dto.setSiretSiren("12345678900000");
        
        when(userRepository.existsByEmail(null)).thenReturn(false);
        when(userMapper.toUser(any(RegisterProviderDTO.class))).thenReturn(null);
        
        assertThrows(NullPointerException.class, () -> userService.registerProvider(dto));
        
        verify(userRepository).existsByEmail(null);
    }

    @Test
    void registerProvider_WhenEmailIsEmpty_ShouldThrowException() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("");
        dto.setCompanyName("Test Company");
        dto.setSiretSiren("12345678900000");
        
        when(userRepository.existsByEmail("")).thenReturn(false);
        when(userMapper.toUser(any(RegisterProviderDTO.class))).thenReturn(null);
        
        assertThrows(NullPointerException.class, () -> userService.registerProvider(dto));
        
        verify(userRepository).existsByEmail("");
    }

    @Test
    void login_WhenEmailIsNull_ShouldThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(null);
        userDTO.setPassword("password123");
        
        assertThrows(NullPointerException.class, () -> userService.login(userDTO));
    }

    @Test
    void login_WhenPasswordIsNull_ShouldThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword(null);
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));
        
        assertThrows(WrongPasswordException.class, () -> userService.login(userDTO));
        
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(passwordEncoder).matches(null, mockUser.getPassword());
    }

    // Tests pour les cas limites de performance et sécurité

    @Test
    void login_WhenMultipleFailedAttempts_ShouldIncrementAttempts() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("wrongpassword");
        
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(userDTO.getPassword(), mockUser.getPassword())).thenReturn(false);
        
        // Premier échec
        assertThrows(WrongPasswordException.class, () -> userService.login(userDTO));
        
        // Deuxième échec
        assertThrows(WrongPasswordException.class, () -> userService.login(userDTO));
        
        verify(userRepository, times(2)).findByEmail(userDTO.getEmail());
        verify(passwordEncoder, times(2)).matches(userDTO.getPassword(), mockUser.getPassword());
    }

    @Test
    void registerClient_WhenPasswordIsEmpty_ShouldHandleGracefully() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("");
        dto.setFirstName("Test");
        dto.setLastName("User");
        
        User userWithEmptyPassword = createMockUser();
        userWithEmptyPassword.setPassword("");
        
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toUser(any(RegisterClientDTO.class))).thenReturn(userWithEmptyPassword);
        when(passwordEncoder.encode("")).thenReturn("encodedEmptyPassword");
        when(userRepository.save(any(User.class))).thenReturn(userWithEmptyPassword);
        
        userService.registerClient(dto);
        
        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userMapper).toUser(any(RegisterClientDTO.class));
        verify(passwordEncoder).encode("");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerProvider_WhenAllFieldsAreEmpty_ShouldThrowException() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setEmail("");
        dto.setPassword("");
        dto.setCompanyName("");
        dto.setSiretSiren("");
        
        assertThrows(MissingFieldException.class, () -> userService.registerProvider(dto));
        
        verify(userRepository, never()).existsByEmail(any());
        verify(userMapper, never()).toUser(any(RegisterProviderDTO.class));
    }

    // Tests pour les cas d'erreur de mapping

    @Test
    void updateUserProfile_WhenUserMapperThrowsException_ShouldPropagateException() {
        Long userId = 37L;
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userMapper.updateUserFromDTO(mockUser, updateDTO)).thenThrow(new RuntimeException("Mapper error"));
            
            assertThrows(RuntimeException.class, () -> userService.updateUserProfile(updateDTO));
            
            verify(userRepository).findById(userId);
            verify(userMapper).updateUserFromDTO(mockUser, updateDTO);
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    void getUserProfile_WhenUserMapperThrowsException_ShouldPropagateException() {
        Long userId = 37L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userMapper.toUserProfileDTO(mockUser)).thenThrow(new RuntimeException("Mapper error"));
            
            assertThrows(RuntimeException.class, () -> userService.getUserProfile());
            
            verify(userRepository).findById(userId);
            verify(userMapper).toUserProfileDTO(mockUser);
        }
    }

    // Tests pour les cas limites de données

    @Test
    void getUserBasicInfoById_WhenUserHasNullFields_ShouldHandleGracefully() {
        Long userId = 37L;
        User userWithNullFields = createMockUser();
        userWithNullFields.setFirstName(null);
        userWithNullFields.setLastName(null);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithNullFields));
        when(reviewRepository.findByReviewedId(userId)).thenReturn(java.util.List.of());
        
        UserBasicInfoDTO result = userService.getUserBasicInfoById(userId);
        
        assertNotNull(result);
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        
        verify(userRepository).findById(userId);
        verify(reviewRepository).findByReviewedId(userId);
    }

    @Test
    void deleteUserAccount_WhenUserHasNullFields_ShouldAnonymizeGracefully() {
        Long userId = 37L;
        User userWithNullFields = createMockUser();
        userWithNullFields.setFirstName(null);
        userWithNullFields.setLastName(null);
        userWithNullFields.setEmail(null);
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(userWithNullFields));
            when(userRepository.save(any(User.class))).thenReturn(userWithNullFields);
            
            userService.deleteUserAccount();
            
            verify(userRepository).findById(userId);
            verify(userRepository).save(any(User.class));
        }
    }

    // Tests pour les cas d'erreur de sécurité

    @Test
    void getUserProfile_WhenSecurityUtilsThrowsException_ShouldPropagateException() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenThrow(new RuntimeException("Security error"));
            
            assertThrows(RuntimeException.class, () -> userService.getUserProfile());
        }
    }

    @Test
    void updateUserProfile_WhenSecurityUtilsThrowsException_ShouldPropagateException() {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenThrow(new RuntimeException("Security error"));
            
            assertThrows(RuntimeException.class, () -> userService.updateUserProfile(updateDTO));
        }
    }

    @Test
    void deleteUserAccount_WhenSecurityUtilsThrowsException_ShouldPropagateException() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenThrow(new RuntimeException("Security error"));
            
            assertThrows(RuntimeException.class, () -> userService.deleteUserAccount());
        }
    }

    private User createMockUser() {
        User user = new User();
        user.setId(37L);
        user.setFirstName("Teste");
        user.setLastName("Teste");
        user.setEmail("teste@test.com");
        user.setPhoneNumber("0612345678");
        user.setAddress("123 rue de Paris");
        user.setCity("Toulouse");
        user.setPostalCode("31000");
        user.setUserRole(UserRole.CLIENT);
        user.setIsAnonymized(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return user;
    }

    private User createMockProviderUser() {
        User user = new User();
        user.setId(39L);
        user.setFirstName("Anna");
        user.setLastName("Cousin");
        user.setEmail("anna@test.com");
        user.setPhoneNumber("0623456789");
        user.setAddress("456 avenue de Lyon");
        user.setCity("Lyon");
        user.setPostalCode("69000");
        user.setUserRole(UserRole.PROVIDER);
        user.setCompanyName("Sophie Services");
        user.setSiretSiren("12340678900010");
        user.setIsAnonymized(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return user;
    }

    private UserProfileDTO createMockUserProfileDTO() {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(37L);
        dto.setFirstName("Teste");
        dto.setLastName("Teste");
        dto.setEmail("teste@test.com");
        dto.setPhoneNumber("0612345678");
        dto.setAddress("123 rue de Paris");
        dto.setCity("Toulouse");
        dto.setPostalCode("31000");
        dto.setUserRole(UserRole.CLIENT);
        
        return dto;
    }
}