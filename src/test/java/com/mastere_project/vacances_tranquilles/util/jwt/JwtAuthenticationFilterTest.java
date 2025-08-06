package com.mastere_project.vacances_tranquilles.util.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtConfig jwtConfig;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        filter = new JwtAuthenticationFilter(jwtConfig);
        SecurityContextHolder.clearContext();
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("doFilterInternal - should authenticate with valid token")
    void doFilterInternal_shouldAuthenticateWithValidToken() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String email = "user@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/test");
        when(jwtConfig.extractEmail(token)).thenReturn(email);
        when(jwtConfig.extractUserId(token)).thenReturn(1L);
        when(jwtConfig.validateToken(token, 1L)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        assert authentication.getPrincipal().equals(1L);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilterInternal - should not authenticate with invalid token")
    void doFilterInternal_shouldNotAuthenticateWithInvalidToken() throws ServletException, IOException {
        String token = "invalid.jwt.token";
        String email = "user@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/test");
        when(jwtConfig.extractEmail(token)).thenReturn(email);
        when(jwtConfig.extractUserId(token)).thenReturn(1L);
        when(jwtConfig.validateToken(token, 1L)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }

    @Test
    @DisplayName("doFilterInternal - should handle missing Authorization header")
    void doFilterInternal_shouldSkipIfNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/test");

        filter.doFilterInternal(request, response, filterChain);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }

    @Test
    @DisplayName("doFilterInternal - should handle Authorization header that does not start with Bearer")
    void doFilterInternal_shouldSkipIfHeaderNotBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic something");
        when(request.getRequestURI()).thenReturn("/api/test");

        filter.doFilterInternal(request, response, filterChain);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }

    @Test
    @DisplayName("doFilterInternal - should skip if already authenticated")
    void doFilterInternal_shouldSkipIfAlreadyAuthenticated() throws ServletException, IOException {
        Authentication mockAuth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(mockAuth);
        String token = "valid.jwt.token";
        String email = "user@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/test");
        when(jwtConfig.extractEmail(token)).thenReturn(email);
        when(jwtConfig.validateToken(token, 1L)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        // L'authentification ne doit pas être remplacée
        assert SecurityContextHolder.getContext().getAuthentication() == mockAuth;
        verify(filterChain).doFilter(request, response);
    }
}