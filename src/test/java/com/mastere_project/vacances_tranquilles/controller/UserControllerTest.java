package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.service.UserService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
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
        // Given
        UserProfileDTO expectedProfile = createMockUserProfile();
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userService.getUserProfile(1L)).thenReturn(expectedProfile);
            
            // When
            ResponseEntity<UserProfileDTO> response = userController.getUserProfile();
            
            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(expectedProfile, response.getBody());
            verify(userService).getUserProfile(1L);
        }
    }

    @Test
    void updateUserProfile_ShouldReturnUpdatedProfile() {
        // Given
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau");
        UserProfileDTO expectedProfile = createMockUserProfile();
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userService.updateUserProfile(1L, updateDTO)).thenReturn(expectedProfile);
            
            // When
            ResponseEntity<UserProfileDTO> response = userController.updateUserProfile(updateDTO);
            
            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(expectedProfile, response.getBody());
            verify(userService).updateUserProfile(1L, updateDTO);
        }
    }

    @Test
    void deleteUserAccount_ShouldReturnNoContent() {
        // Given
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            doNothing().when(userService).deleteUserAccount(1L);
            
            // When
            ResponseEntity<Void> response = userController.deleteUserAccount();
            
            // Then
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(userService).deleteUserAccount(1L);
        }
    }

    @Test
    void getUserProfile_WhenUserNotFound_ShouldThrowAccessDeniedException() {
        // Given
        Long nonExistentUserId = 999L;
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(nonExistentUserId);
            
            when(userService.getUserProfile(nonExistentUserId))
                    .thenThrow(new AccessDeniedException("Utilisateur non trouvé"));
            
            // When & Then
            AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
                userController.getUserProfile();
            });
            
            // Verify
            verify(userService).getUserProfile(nonExistentUserId);
            assertEquals("Utilisateur non trouvé", exception.getMessage());
        }
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