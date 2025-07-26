package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.DeleteAccountDTO;
import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private Principal mockPrincipal;
    private UserProfileDTO mockUserProfile;

    @BeforeEach
    void setUp() {
        mockPrincipal = new UsernamePasswordAuthenticationToken("37", null);
        mockUserProfile = createMockUserProfile();
    }

    @Test
    void getAllClients_ShouldReturnClientsList() {
        List<UserProfileDTO> expectedClients = Arrays.asList(mockUserProfile);
        when(userService.getAllClients()).thenReturn(expectedClients);

        ResponseEntity<List<UserProfileDTO>> response = userController.getAllClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClients, response.getBody());
        verify(userService).getAllClients();
    }

    @Test
    void getAllProviders_ShouldReturnProvidersList() {
        List<UserProfileDTO> expectedProviders = Arrays.asList(mockUserProfile);
        when(userService.getAllProviders()).thenReturn(expectedProviders);

        ResponseEntity<List<UserProfileDTO>> response = userController.getAllProviders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProviders, response.getBody());
        verify(userService).getAllProviders();
    }

    @Test
    void getClientById_ShouldReturnClient() {
        Long clientId = 37L;
        when(userService.getClientById(clientId)).thenReturn(mockUserProfile);

        ResponseEntity<UserProfileDTO> response = userController.getClientById(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserProfile, response.getBody());
        verify(userService).getClientById(clientId);
    }

    @Test
    void getClientById_WhenClientNotFound_ShouldReturn404() {
        Long clientId = 999L;
        when(userService.getClientById(clientId)).thenThrow(new UserNotFoundException("Client non trouvé"));

        ResponseEntity<UserProfileDTO> response = userController.getClientById(clientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getClientById(clientId);
    }

    @Test
    void getProviderById_ShouldReturnProvider() {
        Long providerId = 39L;
        when(userService.getProviderById(providerId)).thenReturn(mockUserProfile);

        ResponseEntity<UserProfileDTO> response = userController.getProviderById(providerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserProfile, response.getBody());
        verify(userService).getProviderById(providerId);
    }

    @Test
    void getProviderById_WhenProviderNotFound_ShouldReturn404() {
        Long providerId = 999L;
        when(userService.getProviderById(providerId)).thenThrow(new UserNotFoundException("Prestataire non trouvé"));

        ResponseEntity<UserProfileDTO> response = userController.getProviderById(providerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getProviderById(providerId);
    }

    @Test
    void getUserProfile_ShouldReturnUserProfile() {
        when(userService.getUserProfile(37L)).thenReturn(mockUserProfile);

        ResponseEntity<UserProfileDTO> response = userController.getUserProfile(mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserProfile, response.getBody());
        verify(userService).getUserProfile(37L);
    }

    @Test
    void getUserProfile_WhenUserNotFound_ShouldReturn404() {
        when(userService.getUserProfile(37L)).thenThrow(new UserNotFoundException("Utilisateur non trouvé"));

        ResponseEntity<UserProfileDTO> response = userController.getUserProfile(mockPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserProfile(37L);
    }

    @Test
    void updateUserProfile_ShouldReturnUpdatedProfile() {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        when(userService.updateUserProfile(37L, updateDTO)).thenReturn(mockUserProfile);

        ResponseEntity<UserProfileDTO> response = userController.updateUserProfile(updateDTO, mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserProfile, response.getBody());
        verify(userService).updateUserProfile(37L, updateDTO);
    }

    @Test
    void updateUserProfile_WhenUserNotFound_ShouldReturn404() {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        when(userService.updateUserProfile(37L, updateDTO)).thenThrow(new UserNotFoundException("Utilisateur non trouvé"));

        ResponseEntity<UserProfileDTO> response = userController.updateUserProfile(updateDTO, mockPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).updateUserProfile(37L, updateDTO);
    }

    @Test
    void updateUserProfile_WhenError_ShouldReturn400() {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau Prénom");
        when(userService.updateUserProfile(37L, updateDTO)).thenThrow(new RuntimeException("Erreur"));

        ResponseEntity<UserProfileDTO> response = userController.updateUserProfile(updateDTO, mockPrincipal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).updateUserProfile(37L, updateDTO);
    }

    @Test
    void deleteUserAccount_ShouldReturn204() {
        DeleteAccountDTO deleteAccountDTO = new DeleteAccountDTO();
        deleteAccountDTO.setReason("Test de suppression");
        doNothing().when(userService).deleteUserAccount(37L, deleteAccountDTO);

        ResponseEntity<Void> response = userController.deleteUserAccount(deleteAccountDTO, mockPrincipal);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).deleteUserAccount(37L, deleteAccountDTO);
    }

    @Test
    void deleteUserAccount_WithoutDTO_ShouldUseDefaultValues() {
        doNothing().when(userService).deleteUserAccount(eq(37L), any(DeleteAccountDTO.class));

        ResponseEntity<Void> response = userController.deleteUserAccount(null, mockPrincipal);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).deleteUserAccount(eq(37L), any(DeleteAccountDTO.class));
    }

    @Test
    void deleteUserAccount_WhenUserNotFound_ShouldReturn404() {
        DeleteAccountDTO deleteAccountDTO = new DeleteAccountDTO();
        deleteAccountDTO.setReason("Test de suppression");
        doThrow(new UserNotFoundException("Utilisateur non trouvé")).when(userService).deleteUserAccount(37L, deleteAccountDTO);

        ResponseEntity<Void> response = userController.deleteUserAccount(deleteAccountDTO, mockPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).deleteUserAccount(37L, deleteAccountDTO);
    }

    @Test
    void deleteUserAccount_WhenError_ShouldReturn400() {
        DeleteAccountDTO deleteAccountDTO = new DeleteAccountDTO();
        deleteAccountDTO.setReason("Test de suppression");
        doThrow(new RuntimeException("Erreur")).when(userService).deleteUserAccount(37L, deleteAccountDTO);

        ResponseEntity<Void> response = userController.deleteUserAccount(deleteAccountDTO, mockPrincipal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).deleteUserAccount(37L, deleteAccountDTO);
    }

    private UserProfileDTO createMockUserProfile() {
        UserProfileDTO userProfile = new UserProfileDTO();
        userProfile.setId(37L);
        userProfile.setFirstName("Teste");
        userProfile.setLastName("Teste");
        userProfile.setEmail("teste@test.com");
        userProfile.setUserRole(UserRole.CLIENT);
        userProfile.setPhoneNumber("0612345678");
        userProfile.setAddress("123 rue de Paris");
        userProfile.setCity("Toulouse");
        userProfile.setPostalCode("31000");

        return userProfile;
    }
} 