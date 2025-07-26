package com.mastere_project.vacances_tranquilles.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mastere_project.vacances_tranquilles.util.jwt.JwtAuthenticationFilter;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;

import lombok.RequiredArgsConstructor;

/**
 * Configuration de la sécurité Spring Security pour l'application.
 * Définit les filtres, l'encodage des mots de passe et les règles d'accès.
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtConfig jwt;

    /**
     * Fournit un encodeur de mots de passe utilisant BCrypt.
     * 
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit le filtre d'authentification JWT.
     *
     * @return le filtre d'authentification JWT
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwt);
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP pour l'application.
     *
     * @param http l'objet de configuration de la sécurité HTTP
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception si une erreur de configuration survient
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Autorise login et register
                        .requestMatchers("/api/users/**").authenticated() // Routes utilisateur protégées
                        .anyRequest().authenticated() // Tout le reste protégé
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}