package com.mastere_project.vacances_tranquilles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserBasicInfoDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUserProfile_ShouldReturnUserProfile() throws Exception {
        // Arrange
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setEmail("test@example.com");
        profileDTO.setFirstName("Test");
        profileDTO.setLastName("User");

        when(userService.getUserProfile()).thenReturn(profileDTO);

        // Act & Assert
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));

        verify(userService).getUserProfile();
    }

    @Test
    void updateUserProfile_ShouldUpdateUserProfile() throws Exception {
        // Arrange
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Updated");
        updateDTO.setLastName("Name");

        UserProfileDTO updatedProfile = new UserProfileDTO();
        updatedProfile.setEmail("test@example.com");
        updatedProfile.setFirstName("Updated");
        updatedProfile.setLastName("Name");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(updatedProfile);

        // Act & Assert
        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"));

        verify(userService).updateUserProfile(any(UpdateUserDTO.class));
    }

    @Test
    void deleteUserAccount_ShouldDeleteUserAccount() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUserAccount();

        // Act & Assert
        mockMvc.perform(delete("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Account deletion completed. All personal data has been deleted or anonymized."));

        verify(userService).deleteUserAccount();
    }

    @Test
    void getUserById_ShouldReturnUserBasicInfo() throws Exception {
        // Arrange
        UserBasicInfoDTO userBasicInfo = new UserBasicInfoDTO();
        userBasicInfo.setFirstName("Test");
        userBasicInfo.setLastName("User");

        when(userService.getUserBasicInfoById(anyLong())).thenReturn(userBasicInfo);

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));

        verify(userService).getUserBasicInfoById(1L);
    }

    // Tests pour les cas d'erreur

    @Test
    void updateUserProfile_WithInvalidJson_ShouldReturn400() throws Exception {
        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserProfile_WithEmptyBody_ShouldReturn200() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setEmail("test@example.com");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(profileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserProfile_WithNullValues_ShouldReturn200() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setEmail("test@example.com");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(profileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":null,\"lastName\":null}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserProfile_WithVeryLongValues_ShouldReturn200() throws Exception {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("a".repeat(1000));
        updateDTO.setLastName("b".repeat(1000));

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setEmail("test@example.com");
        profileDTO.setFirstName("a".repeat(1000));
        profileDTO.setLastName("b".repeat(1000));

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(profileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserProfile_WithSpecialCharacters_ShouldReturn200() throws Exception {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("José");
        updateDTO.setLastName("O'Connor");

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setEmail("test@example.com");
        profileDTO.setFirstName("José");
        profileDTO.setLastName("O'Connor");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(profileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"999999999", "0", "-1"})
    void getUserById_WithVariousIds_ShouldWork(String userId) throws Exception {
        UserBasicInfoDTO userBasicInfo = new UserBasicInfoDTO();
        userBasicInfo.setFirstName("Test");
        userBasicInfo.setLastName("User");

        when(userService.getUserBasicInfoById(anyLong())).thenReturn(userBasicInfo);

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserProfile_WithAllFields_ShouldWork() throws Exception {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("John");
        updateDTO.setLastName("Doe");
        updateDTO.setCompanyName("Test Company");
        updateDTO.setSiretSiren("12345678900000");

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setEmail("john.doe@example.com");
        profileDTO.setFirstName("John");
        profileDTO.setLastName("Doe");
        profileDTO.setCompanyName("Test Company");
        profileDTO.setSiretSiren("12345678900000");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(profileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.companyName").value("Test Company"))
                .andExpect(jsonPath("$.siretSiren").value("12345678900000"));
    }

    @Test
    void updateUserProfile_WithMalformedJson_ShouldReturn400() throws Exception {
        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"John\", }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserProfile_WithWrongContentType_ShouldReturn400() throws Exception {
        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.TEXT_PLAIN)
                .content("plain text"))
                .andExpect(status().isUnsupportedMediaType());
    }



    @Test
    void updateUserProfile_WithEmptyStringValues_ShouldWork() throws Exception {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("");
        updateDTO.setLastName("");

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setEmail("test@example.com");
        profileDTO.setFirstName("");
        profileDTO.setLastName("");

        when(userService.updateUserProfile(any(UpdateUserDTO.class))).thenReturn(profileDTO);

        mockMvc.perform(patch("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(""))
                .andExpect(jsonPath("$.lastName").value(""));
    }

} 