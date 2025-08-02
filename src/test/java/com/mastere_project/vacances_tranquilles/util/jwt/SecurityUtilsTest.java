package com.mastere_project.vacances_tranquilles.util.jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityUtilsTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Doit retourner l'ID utilisateur si authentifié avec un principal Long")
    void getCurrentUserId_ReturnsUserId_WhenAuthenticatedWithLong() {
        // Arrange
        Long expectedId = 42L;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(expectedId);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        // Act
        Long result = SecurityUtils.getCurrentUserId();

        // Assert
        assertEquals(expectedId, result);
    }

    @Test
    @DisplayName("Doit lever AccessDeniedException si non authentifié")
    void getCurrentUserId_ThrowsAccessDenied_WhenNotAuthenticated() {
        // Arrange
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(context);

        // Act & Assert
        assertThrows(AccessDeniedException.class, SecurityUtils::getCurrentUserId);
    }

    @Test
    @DisplayName("Doit lever AccessDeniedException si principal n'est pas un Long")
    void getCurrentUserId_ThrowsAccessDenied_WhenPrincipalNotLong() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("notALong");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        // Act & Assert
        assertThrows(AccessDeniedException.class, SecurityUtils::getCurrentUserId);
    }
}
