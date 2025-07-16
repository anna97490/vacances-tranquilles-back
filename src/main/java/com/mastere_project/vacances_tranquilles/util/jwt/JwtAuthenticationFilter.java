package com.mastere_project.vacances_tranquilles.util.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre d'authentification JWT pour valider les tokens sur chaque requête HTTP.
 * Extrait le token JWT de l'en-tête Authorization, le valide et place l'utilisateur dans le contexte de sécurité Spring.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwt;

    /**
     * Intercepte chaque requête HTTP pour vérifier la présence et la validité d'un token JWT.
     * Si le token est valide, l'utilisateur est authentifié dans le contexte de sécurité.
     * @param request la requête HTTP entrante
     * @param response la réponse HTTP sortante
     * @param filterChain la chaîne de filtres
     * @throws ServletException en cas d'erreur de filtre
     * @throws IOException en cas d'erreur d'E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        Long userId = null;

        // Extraction du token JWT depuis l'en-tête Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userId = jwt.extractUserId(token);
        }

        // Si l'id est extrait et aucune authentification encore définie, alors
        // valider le token
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwt.validateToken(token, userId)) {
                // Création de l'authentification Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,
                        null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Stockage de l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
} 