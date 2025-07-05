package com.mastere_project.vacances_tranquilles.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;
import io.github.cdimascio.dotenv.Dotenv;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;

/**
 * Composant utilitaire pour la gestion des tokens JWT : génération, extraction et validation.
 */
@Component
public class JwtConfig {

    // Chargement de la configuration .env
    private static final Dotenv dotenv = Dotenv.load();

    // Clé secrète utilisée pour signer les tokens JWT.
    private final String secretKey = dotenv.get("JWT_SECRET");

    // Durée de validité du token JWT, ici : 1 heure (60 * 60 * 1000 ms).
    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    /**
     * Génère un token JWT pour un utilisateur donné et son rôle.
     * @param email l'email de l'utilisateur
     * @param role le rôle de l'utilisateur
     * @return le token JWT généré
     */
    public String generateToken(String email, UserRole role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Extrait le rôle utilisateur depuis un token JWT.
     * @param token le token JWT
     * @return le rôle utilisateur
     * @throws IllegalArgumentException si le rôle n'est pas présent dans le token
     */
    public UserRole extractRole(String token) {
        String roleName = extractClaim(token, claims -> claims.get("role", String.class));
        if (roleName == null) {
            throw new IllegalArgumentException("Le rôle n'est pas présent dans le token JWT.");
        }
        return UserRole.valueOf(roleName);
    }


    /**
     * Extrait l'email (subject) depuis un token JWT.
     * @param token le token JWT
     * @return l'email extrait
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Valide un token JWT pour un utilisateur donné.
     * @param token le token JWT
     * @param userEmail l'email attendu
     * @return true si le token est valide et correspond à l'email, false sinon
     */
    public boolean validateToken(String token, String userEmail) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(userEmail) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("La clé secrète JWT doit faire au moins 32 caractères pour HS256.");
        }
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
} 