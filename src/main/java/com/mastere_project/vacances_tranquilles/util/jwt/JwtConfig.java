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
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    // Clé secrète utilisée pour signer les tokens JWT.
    private final String secretKey = dotenv.get("JWT_SECRET", "defaultSecretKeyForTesting12345678901234567890123456789012");
// retirer recup de dotenv car pas de .env fonctionnel
    // Durée de validité du token JWT, ici : 1 heure (60 * 60 * 1000 ms).
    private static final long EXPIRATION_TIME_MS = 60L * 60 * 1000;

    /**
     * Génère un token JWT pour un utilisateur donné (id et rôle).
     *
     * @param id l'identifiant de l'utilisateur
     * @param role le rôle de l'utilisateur
     * @return le token JWT généré
     */
    public String generateToken(Long id, UserRole role) {
        return Jwts.builder()
                .setSubject(String.valueOf(id)) // l'id devient le subject
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Extrait l'id utilisateur depuis un token JWT (subject).
     *
     * @param token le token JWT
     * @return l'id utilisateur extrait du token
     */
    public Long extractUserId(String token) {
        return Long.parseLong(extractClaim(token, Claims::getSubject));
    }


    /**
     * Extrait le rôle utilisateur depuis un token JWT.
     *
     * @param token le token JWT
     * @return le rôle utilisateur extrait du token
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
     *
     * @param token le token JWT
     * @return l'email extrait du token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    
    /**
     * Valide un token JWT pour un utilisateur donné (id).
     *
     * @param token le token JWT
     * @param userId l'identifiant attendu de l'utilisateur
     * @return true si le token est valide et correspond à l'id, false sinon
     */
    public boolean validateToken(String token, Long userId) {
        final Long extractedId = extractUserId(token);
        return (extractedId.equals(userId) && !isTokenExpired(token));
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