package com.mastere_project.vacances_tranquilles.configuration;

import com.mastere_project.vacances_tranquilles.util.jwt.JwtAuthenticationFilter;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private JwtConfig jwtConfig;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        jwtConfig = mock(JwtConfig.class);
        securityConfig = new SecurityConfig(jwtConfig);
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertThat(encoder).isNotNull();
        assertThat(encoder.getClass().getSimpleName()).isEqualTo("BCryptPasswordEncoder");
    }

    @Test
    void jwtAuthenticationFilter_shouldReturnJwtAuthenticationFilterWithJwtConfig() {
        JwtAuthenticationFilter filter = securityConfig.jwtAuthenticationFilter();
        assertThat(filter).isNotNull();
    }

    @Test
    void securityFilterChain_shouldConfigureHttpSecurity() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        // Mock chained methods
        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.build()).thenReturn(mock(org.springframework.security.web.DefaultSecurityFilterChain.class));

        SecurityFilterChain chain = securityConfig.securityFilterChain(http);

        assertThat(chain).isNotNull();

        // Verify configuration methods are called
        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).sessionManagement(any());
        verify(http).addFilterBefore(any(JwtAuthenticationFilter.class),
                eq(UsernamePasswordAuthenticationFilter.class));
        verify(http).build();
    }
}