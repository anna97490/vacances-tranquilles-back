package com.mastere_project.vacances_tranquilles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastere_project.vacances_tranquilles.dto.LoginResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.dto.UserDTO;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        AuthController authController = new AuthController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerClient_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        doNothing().when(userService).registerClient(any(RegisterClientDTO.class));

        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Martin");
        dto.setEmail("alice.martin@example.com");
        dto.setPassword("password123");
        dto.setPhoneNumber("0612345678");
        dto.setAddress("12 rue de Paris");
        dto.setPostalCode("75001");
        dto.setCity("Paris");

        String requestBody = objectMapper.writeValueAsString(dto);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Client registered successfully"));

        verify(userService, times(1)).registerClient(any(RegisterClientDTO.class));
    }

    @Test
    void registerProvider_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        doNothing().when(userService).registerProvider(any(RegisterProviderDTO.class));

        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setFirstName("Bob");
        dto.setLastName("Durand");
        dto.setEmail("bob.durand@example.com");
        dto.setPassword("superPassword!");
        dto.setPhoneNumber("0622334455");
        dto.setAddress("45 avenue du Général");
        dto.setPostalCode("69003");
        dto.setCity("Lyon");
        dto.setCompanyName("Durand & Fils");
        dto.setSiretSiren("12345678900015");

        String requestBody = objectMapper.writeValueAsString(dto);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Provider registered successfully"));

        verify(userService, times(1)).registerProvider(any(RegisterProviderDTO.class));
    }

    @Test
    void login_shouldReturnLoginResponseDTO() throws Exception {
        // Arrange
        LoginResponseDTO loginResponse = new LoginResponseDTO("token123", UserRole.CLIENT);
        when(userService.login(any(UserDTO.class))).thenReturn(loginResponse);

        String requestBody = """
                    {
                        "email": "test@example.com",
                        "password": "password"
                    }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));

        verify(userService, times(1)).login(any(UserDTO.class));
    }

}