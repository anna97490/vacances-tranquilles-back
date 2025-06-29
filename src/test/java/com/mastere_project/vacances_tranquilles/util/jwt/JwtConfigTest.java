package com.mastere_project.vacances_tranquilles.util.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class JwtConfigTest {

    private JwtConfig jwtConfig;
    private final String SECRET = "12345678901234567890123456789012"; // 32 chars

    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfig();
        ReflectionTestUtils.setField(jwtConfig, "secretKey", SECRET);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String email = "test@example.com";
        String token = jwtConfig.generateToken(email);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractEmail_shouldReturnCorrectEmail() {
        String email = "user@domain.com";
        String token = jwtConfig.generateToken(email);
        String extracted = jwtConfig.extractEmail(token);
        assertEquals(email, extracted);
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String email = "valid@domain.com";
        String token = jwtConfig.generateToken(email);
        assertTrue(jwtConfig.validateToken(token, email));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidEmail() {
        String email = "user@domain.com";
        String token = jwtConfig.generateToken(email);
        assertFalse(jwtConfig.validateToken(token, "other@domain.com"));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        // Create a token with a very short expiration
        ReflectionTestUtils.setField(jwtConfig, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtConfig, "EXPIRATION_TIME_MS", 1L); // 1 ms
        String email = "expired@domain.com";
        String token = jwtConfig.generateToken(email);
        Thread.sleep(5); // Wait for token to expire
        assertFalse(jwtConfig.validateToken(token, email));
    }

    @Test
    void getSigningKey_shouldThrowExceptionForShortSecret() {
        JwtConfig config = new JwtConfig();
        ReflectionTestUtils.setField(config, "secretKey", "shortsecret");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> config.generateToken("test@domain.com"));
        assertTrue(exception.getMessage().contains("JWT secret key must be at least 256 bits"));
    }
}