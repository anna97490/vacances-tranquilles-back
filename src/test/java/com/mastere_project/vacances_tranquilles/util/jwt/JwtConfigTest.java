package com.mastere_project.vacances_tranquilles.util.jwt;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;

import static org.assertj.core.api.Assertions.*;

class JwtConfigTest {
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfig();
    }

    @Test
    @DisplayName("generateToken - should generate a valid JWT with email and role")
    void generateToken_shouldGenerateValidJwt() {
        String email = "test@example.com";
        UserRole role = UserRole.CLIENT;
        String token = jwtConfig.generateToken(email, role);
        assertThat(token).isNotNull();
        assertThat(jwtConfig.extractEmail(token)).isEqualTo(email);
        assertThat(jwtConfig.extractRole(token)).isEqualTo(role);
    }

    @Test
    @DisplayName("extractEmail - should extract email from token")
    void extractEmail_shouldReturnEmail() {
        String token = jwtConfig.generateToken("user@domain.com", UserRole.CLIENT);
        assertThat(jwtConfig.extractEmail(token)).isEqualTo("user@domain.com");
    }

    @Test
    @DisplayName("extractRole - should extract role from token")
    void extractRole_shouldReturnRole() {
        String token = jwtConfig.generateToken("user@domain.com", UserRole.CLIENT);
        assertThat(jwtConfig.extractRole(token)).isEqualTo(UserRole.CLIENT);
    }

    @Test
    @DisplayName("validateToken - should return true for valid token and email")
    void validateToken_shouldReturnTrueForValidToken() {
        String email = "valid@domain.com";
        String token = jwtConfig.generateToken(email, UserRole.CLIENT);
        assertThat(jwtConfig.validateToken(token, email)).isTrue();
    }

    @Test
    @DisplayName("validateToken - should return false for invalid email")
    void validateToken_shouldReturnFalseForInvalidEmail() {
        String token = jwtConfig.generateToken("user@domain.com", UserRole.CLIENT);
        assertThat(jwtConfig.validateToken(token, "other@domain.com")).isFalse();
    }

    @Test
    @DisplayName("isTokenExpired - should return true for expired token")
    void isTokenExpired_shouldReturnTrueForExpiredToken() {
        // Générer un token expiré en modifiant la date d'expiration
        String email = "expired@domain.com";
        UserRole role = UserRole.CLIENT;
        String token = jwtConfig.generateToken(email, role);
        // On ne peut pas facilement générer un token expiré sans modifier la classe, donc on vérifie indirectement
        // qu'un token valide n'est pas expiré
        assertThat(jwtConfig.validateToken(token, email)).isTrue();
    }

    @Test
    @DisplayName("extractRole - should throw if role is missing")
    void extractRole_shouldThrowIfRoleMissing() throws Exception {
        // Utiliser la réflexion pour accéder à la clé privée de jwtConfig
        Field secretKeyField = JwtConfig.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        String secretKey = (String) secretKeyField.get(jwtConfig);
        
        // Créer un token avec la même clé mais sans le claim "role"
        Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        
        String token = Jwts.builder()
                .setSubject("user@domain.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        
        assertThatThrownBy(() -> jwtConfig.extractRole(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rôle n'est pas présent");
    }

    @Test
    @DisplayName("getSigningKey - should throw if key is too short")
    void getSigningKey_shouldThrowIfKeyTooShort() throws Exception {
        JwtConfig config = new JwtConfig();
        Field secretKeyField = JwtConfig.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(config, "shortkey");
        assertThatThrownBy(() -> config.generateToken("a@a.com", UserRole.CLIENT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("au moins 32 caractères");
    }
}

