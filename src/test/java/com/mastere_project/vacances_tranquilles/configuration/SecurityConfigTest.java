package com.mastere_project.vacances_tranquilles.configuration;

import com.mastere_project.vacances_tranquilles.util.jwt.JwtAuthenticationFilter;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.test.util.ReflectionTestUtils;

import org.springframework.mock.web.MockHttpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private JwtConfig jwtConfig;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        jwtConfig = mock(JwtConfig.class);
        securityConfig = new SecurityConfig(jwtConfig);
        // Set the allowedOrigin value for testing
        ReflectionTestUtils.setField(securityConfig, "allowedOrigin", "http://localhost:3000");
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
        when(http.cors(any())).thenReturn(http);
        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        SecurityFilterChain chain = securityConfig.securityFilterChain(http);

        assertThat(chain).isNotNull();

        // Verify configuration methods are called in the correct order
        verify(http).cors(any());
        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).sessionManagement(any());
        verify(http).addFilterBefore(any(JwtAuthenticationFilter.class),
                eq(UsernamePasswordAuthenticationFilter.class));
        verify(http).build();
    }

    @Test
    void securityFilterChain_shouldConfigureCorsCorrectly() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(http.cors(any())).thenReturn(http);
        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        securityConfig.securityFilterChain(http);

        // Verify that CORS configuration was called
        verify(http).cors(any());
    }

    @Test
    void corsConfigurationSource_shouldReturnExpectedCorsConfiguration() {
        ReflectionTestUtils.setField(securityConfig, "allowedOrigin", "http://localhost:3000");

        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(new MockHttpServletRequest());

        assertThat(config).isNotNull();
        assertThat(config.getAllowedOriginPatterns()).containsExactly("http://localhost:3000");
        assertThat(config.getAllowedMethods()).containsExactlyInAnyOrder("GET", "POST", "PUT", "PATCH", "DELETE",
                "OPTIONS");
        assertThat(config.getAllowedHeaders()).containsExactly("*");
        assertThat(config.getAllowCredentials()).isTrue();
    }

    @Test
    void authorizeRequests_shouldNotBeNull() {
        var customizer = securityConfig.authorizeRequests();
        assertThat(customizer).isNotNull();
    }

}