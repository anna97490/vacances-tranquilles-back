package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    void getUserProfile_ShouldReturnUserProfile() {
        UserProfileDTO expectedProfile = createMockUserProfile();
        when(userService.getUserProfile()).thenReturn(expectedProfile);
        
        ResponseEntity<UserProfileDTO> response = userController.getUserProfile();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProfile, response.getBody());
        verify(userService).getUserProfile();
    }

    @Test
    void updateUserProfile_ShouldReturnUpdatedProfile() {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau");
        UserProfileDTO expectedProfile = createMockUserProfile();
        
        when(userService.updateUserProfile(updateDTO)).thenReturn(expectedProfile);
        
        ResponseEntity<UserProfileDTO> response = userController.updateUserProfile(updateDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProfile, response.getBody());
        verify(userService).updateUserProfile(updateDTO);
    }

    @Test
    void deleteUserAccount_ShouldReturnOkWithMessage() {
        // Given
        doNothing().when(userService).deleteUserAccount();
        
        // When
        ResponseEntity<Object> response = userController.deleteUserAccount();
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService).deleteUserAccount();
    }

    @Test
    void getUserProfile_WhenUserNotFound_ShouldThrowAccessDeniedException() {
        when(userService.getUserProfile())
                .thenThrow(new AccessDeniedException("Utilisateur non trouvé"));
        
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            userController.getUserProfile();
        });
        
        // Verify
        verify(userService).getUserProfile();
        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    private UserProfileDTO createMockUserProfile() {
        UserProfileDTO profile = new UserProfileDTO();
        profile.setId(1L);
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setEmail("john.doe@example.com");
        profile.setUserRole(UserRole.CLIENT);
        return profile;
    }
} 