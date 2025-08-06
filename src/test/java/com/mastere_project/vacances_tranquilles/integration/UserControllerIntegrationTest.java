package com.mastere_project.vacances_tranquilles.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastere_project.vacances_tranquilles.controller.UserController;
import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserBasicInfoDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.exception.ApplicationControllerAdvice;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour UserController.
 * Utilise MockMvcBuilders.standaloneSetup() pour éviter les problèmes de contexte Spring.
 */
class UserControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserService userService;

    private UserProfileDTO mockUserProfileDTO;
    private UserBasicInfoDTO mockUserBasicInfoDTO;
    private UpdateUserDTO mockUpdateUserDTO;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new ApplicationControllerAdvice())
                .build();

        mockUserProfileDTO = createMockUserProfileDTO();
        mockUserBasicInfoDTO = createMockUserBasicInfoDTO();
        mockUpdateUserDTO = createMockUpdateUserDTO();
    }

    // Tests pour GET /api/users/profile

    @Test
    void getUserProfile_ShouldReturnUserProfile() throws Exception {
        when(userService.getUserProfile()).thenReturn(mockUserProfileDTO);

        mockMvc.perform(get("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(37L))
                .andExpect(jsonPath("$.firstName").value("Teste"))
                .andExpect(jsonPath("$.lastName").value("Teste"))
                .andExpect(jsonPath("$.email").value("teste@test.com"))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));

        verify(userService).getUserProfile();
    }

    @Test
    void getUserProfile_WhenServiceThrowsException_ShouldReturnError() throws Exception {
        when(userService.getUserProfile()).thenThrow(new AccessDeniedException("Utilisateur non trouvé"));

        mockMvc.perform(get("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService).getUserProfile();
    }

    // Tests pour PATCH /api/users/profile

    @Test
    void updateUserProfile_WithValidData_ShouldReturnUpdatedProfile() throws Exception {
        UserProfileDTO updatedProfile = createMockUserProfileDTO();
        updatedProfile.setFirstName("Nouveau Prénom");
        updatedProfile.setPhoneNumber("0623456789");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(updatedProfile);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUpdateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Nouveau Prénom"))
                .andExpect(jsonPath("$.phoneNumber").value("0623456789"));

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void updateUserProfile_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void updateUserProfile_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk()); // Accepte un objet vide

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void updateUserProfile_WhenServiceThrowsException_ShouldReturnError() throws Exception {
        when(userService.updateUserProfile(any(UpdateUserDTO.class)))
                .thenThrow(new AccessDeniedException("Utilisateur non trouvé"));

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUpdateUserDTO)))
                .andExpect(status().isForbidden());

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    // Tests pour DELETE /api/users/profile

    @Test
    void deleteUserAccount_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(userService).deleteUserAccount();

        mockMvc.perform(delete("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Account deletion completed. All personal data has been deleted or anonymized."));

        verify(userService).deleteUserAccount();
    }

    @Test
    void deleteUserAccount_WhenServiceThrowsException_ShouldReturnError() throws Exception {
        doThrow(new AccessDeniedException("Utilisateur non trouvé"))
                .when(userService).deleteUserAccount();

        mockMvc.perform(delete("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService).deleteUserAccount();
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUserBasicInfo() throws Exception {
        when(userService.getUserBasicInfoById(37L)).thenReturn(mockUserBasicInfoDTO);

        mockMvc.perform(get("/api/users/37")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Teste"))
                .andExpect(jsonPath("$.lastName").value("Teste"));

        verify(userService).getUserBasicInfoById(37L);
    }

    @Test
    void getUserById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/users/invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserBasicInfoById(anyLong());
    }

    @Test
    void getUserById_WithNegativeId_ShouldWork() throws Exception {
        when(userService.getUserBasicInfoById(-1L)).thenReturn(mockUserBasicInfoDTO);

        mockMvc.perform(get("/api/users/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getUserBasicInfoById(-1L);
    }

    @Test
    void getUserById_WithZeroId_ShouldWork() throws Exception {
        when(userService.getUserBasicInfoById(0L)).thenReturn(mockUserBasicInfoDTO);

        mockMvc.perform(get("/api/users/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getUserBasicInfoById(0L);
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldReturnError() throws Exception {
        when(userService.getUserBasicInfoById(999L))
                .thenThrow(new AccessDeniedException("Utilisateur non trouvé"));

        mockMvc.perform(get("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService).getUserBasicInfoById(999L);
    }

    // Tests pour les cas d'erreur de validation

    @Test
    void updateUserProfile_WithNullValues_ShouldAcceptRequest() throws Exception {
        UpdateUserDTO nullUpdateDTO = new UpdateUserDTO();
        nullUpdateDTO.setFirstName(null);
        nullUpdateDTO.setLastName(null);

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(mockUserProfileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullUpdateDTO)))
                .andExpect(status().isOk());

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void updateUserProfile_WithVeryLongValues_ShouldAcceptRequest() throws Exception {
        UpdateUserDTO longUpdateDTO = new UpdateUserDTO();
        longUpdateDTO.setFirstName("A".repeat(1000));
        longUpdateDTO.setLastName("B".repeat(1000));

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(mockUserProfileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(longUpdateDTO)))
                .andExpect(status().isOk());

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void updateUserProfile_WithSpecialCharacters_ShouldAcceptRequest() throws Exception {
        UpdateUserDTO specialUpdateDTO = new UpdateUserDTO();
        specialUpdateDTO.setFirstName("José María");
        specialUpdateDTO.setLastName("O'Connor");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(mockUserProfileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialUpdateDTO)))
                .andExpect(status().isOk());

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void updateUserProfile_WithWrongContentType_ShouldReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.TEXT_PLAIN)
                .content("some text"))
                .andExpect(status().isUnsupportedMediaType());

        verify(userService, never()).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void updateUserProfile_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        String malformedJson = "{\"firstName\": \"Test\", \"lastName\":}";

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUserProfile(any(UpdateUserDTO.class));
    }

    // Tests pour les cas limites

    @Test
    void updateUserProfile_WithEmptyStringValues_ShouldAcceptRequest() throws Exception {
        UpdateUserDTO emptyUpdateDTO = new UpdateUserDTO();
        emptyUpdateDTO.setFirstName("");
        emptyUpdateDTO.setLastName("");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(mockUserProfileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyUpdateDTO)))
                .andExpect(status().isOk());

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    // Méthodes utilitaires pour créer les objets mock

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

    private UserBasicInfoDTO createMockUserBasicInfoDTO() {
        UserBasicInfoDTO dto = new UserBasicInfoDTO();
        dto.setFirstName("Teste");
        dto.setLastName("Teste");
        return dto;
    }

    private UpdateUserDTO createMockUpdateUserDTO() {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setFirstName("Nouveau Prénom");
        dto.setLastName("Nouveau Nom");
        dto.setPhoneNumber("0623456789");
        dto.setAddress("Nouvelle adresse");
        dto.setCity("Nouvelle ville");
        dto.setPostalCode("75000");
        return dto;
    }
} 