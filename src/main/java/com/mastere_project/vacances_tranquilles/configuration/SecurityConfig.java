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
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtConfig jwt;

    /**
     * Fournit le bean PasswordEncoder utilisé pour encoder les mots de passe.
     * @return un PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit le bean JwtAuthenticationFilter pour l'authentification JWT.
     * @return un JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwt);
    }

    /**
     * Configure la chaîne de filtres de sécurité.
     * @param http l'objet HttpSecurity
     * @return la SecurityFilterChain configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
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