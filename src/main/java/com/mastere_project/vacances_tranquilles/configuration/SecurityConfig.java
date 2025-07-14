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
     * @return JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwt);
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     * 
     * @param http la configuration HttpSecurity
     * @return SecurityFilterChain configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
         // CSRF désactivé car l’API est stateless et utilise JWT dans l’en-tête Authorization.
        // Aucun cookie d’authentification n’est utilisé, donc le risque CSRF est inexistant.
        // Voir : https://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/html5/
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
