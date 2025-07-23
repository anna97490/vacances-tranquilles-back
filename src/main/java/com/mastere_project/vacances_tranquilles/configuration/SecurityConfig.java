package com.mastere_project.vacances_tranquilles.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.mastere_project.vacances_tranquilles.util.jwt.JwtAuthenticationFilter;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;

import lombok.RequiredArgsConstructor;

/**
 * Configuration de la sécurité Spring Security pour l'application.
 * Définit les filtres, l'encodage des mots de passe, les règles d'accès et la
 * configuration CORS.
 * 
 * <p>
 * La configuration CORS permet les requêtes cross-origin depuis les origines
 * spécifiées
 * dans la propriété {@code app.cors.allowed-origins} (configurable via variable
 * d'environnement).
 * </p>
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    /**
     * Origines autorisées pour les requêtes CORS.
     * Configurée via {@code app.cors.allowed-origins} (variable d'environnement
     * ALLOWED_ORIGINS).
     */
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigin;

    private final JwtConfig jwt;

    /**
     * Fournit un encodeur de mots de passe utilisant BCrypt.
     * 
     * @return PasswordEncoder configuré avec BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit le filtre d'authentification JWT.
     *
     * @return le filtre d'authentification JWT configuré
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwt);
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP pour l'application.
     * 
     * <p>
     * Inclut : configuration CORS, désactivation CSRF, règles d'autorisation,
     * sessions stateless et filtre JWT.
     * </p>
     *
     * <p>
     * Configuration CORS : origines depuis {@code allowedOrigin}, méthodes
     * GET/POST/PUT/DELETE/OPTIONS,
     * tous headers autorisés, credentials activés.
     * </p>
     *
     * @param http l'objet de configuration de la sécurité HTTP
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception si une erreur de configuration survient
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOriginPatterns(List.of(allowedOrigin.trim()));
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Autorise login et register
                        .anyRequest().authenticated() // Tout le reste protégé
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}