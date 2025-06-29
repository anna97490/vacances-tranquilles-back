package com.mastere_project.vacances_tranquilles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastere_project.vacances_tranquilles.configuration.TestSecurityConfig;
import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Doit enregistrer un client et renvoyer 200")
    void registerClient_ReturnsOk() throws Exception {
        // Arrange
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Martin");
        dto.setEmail("alice.martin@example.com");
        dto.setPassword("password123");
        dto.setPhoneNumber("0612345678");
        dto.setAddress("12 rue de Paris");
        dto.setPostalCode("75001");
        dto.setCity("Paris");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Client registered successfully"));

        verify(userService).registerClient(any(RegisterClientDTO.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Doit enregistrer un prestataire et renvoyer 200")
    void registerProvider_ReturnsOk() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(post("/api/auth/register/provider")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Provider registered successfully"));

        verify(userService).registerProvider(any(RegisterProviderDTO.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Doit renvoyer 400 si DTO client invalide")
    void registerClient_ReturnsBadRequest_WhenInvalid() throws Exception {
        // Arrange
        RegisterClientDTO dto = new RegisterClientDTO(); // Champs manquants

        // Act & Assert
        mockMvc.perform(post("/api/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Doit renvoyer 400 si DTO provider invalide")
    void registerProvider_ReturnsBadRequest_WhenInvalid() throws Exception {
        // Arrange
        RegisterProviderDTO dto = new RegisterProviderDTO(); // Champs manquants

        // Act & Assert
        mockMvc.perform(post("/api/auth/register/provider")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }
}
