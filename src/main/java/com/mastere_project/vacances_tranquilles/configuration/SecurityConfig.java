package com.mastere_project.vacances_tranquilles.configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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

    @Value("${monitoring.username}")
    private String monitoringUsername;

    @Value("${monitoring.password}")
    private String monitoringPassword;

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
     * Fournit la configuration CORS.
     * 
     * Configuration CORS : origines depuis {@code allowedOrigin}, méthodes
     * GET/POST/PUT/DELETE/OPTIONS,
     * tous headers autorisés, credentials activés.
     *
     * @return la configuration CORS configurée
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of(allowedOrigin.trim()));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);
        return request -> corsConfig;
    }

    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeRequests() {
        return auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain prometheusSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/actuator/prometheus")
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP pour l'application.
     *
     * @param http l'objet de configuration de la sécurité HTTP
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception si une erreur de configuration survient
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService monitoringUserDetailsService() {

        UserDetails user = User.builder()
            .username(monitoringUsername)
            .password(passwordEncoder().encode(monitoringPassword))
            .roles("MONITORING")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}