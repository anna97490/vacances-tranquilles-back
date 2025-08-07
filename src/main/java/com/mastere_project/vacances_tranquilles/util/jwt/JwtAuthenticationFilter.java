package com.mastere_project.vacances_tranquilles.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastere_project.vacances_tranquilles.exception.ErrorEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
     *
     * @param request la requête HTTP entrante
     * @param response la réponse HTTP sortante
     * @param filterChain la chaîne de filtres à poursuivre
     * @throws ServletException en cas d'erreur de filtre
     * @throws IOException en cas d'erreur d'E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Ignorer les routes d'authentification
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        Long userId = null;

        // Vérifier si un token est présent
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleTokenError(response, "Token JWT manquant");
            return;
        }

        // Extraction du token
        token = authHeader.substring(7);
        
        // Vérifier si le token n'est pas vide
        if (token.trim().isEmpty()) {
            handleTokenError(response, "Token JWT vide");
            return;
        }
        
        try {
            userId = jwt.extractUserId(token);
        } catch (Exception e) {
            handleTokenError(response, "Token JWT invalide ou expiré");
            return;
        }

        // Si l'id est extrait et aucune authentification encore définie, alors
        // valider le token
        if (userId != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwt.validateToken(token, userId)) {
                    // Création de l'authentification Spring Security
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,
                            null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Stockage de l'authentification dans le contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    handleTokenError(response, "Token JWT expiré");
                    return;
                }
            } catch (Exception e) {
                handleTokenError(response, "Token JWT invalide ou expiré");
                return;
            }
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Gère les erreurs de token en retournant une réponse JSON appropriée.
     *
     * @param response la réponse HTTP
     * @param message le message d'erreur
     * @throws IOException en cas d'erreur d'E/S
     */
    private void handleTokenError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ErrorEntity error = new ErrorEntity("INVALID_TOKEN", message);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(error);
        
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.startsWith("/actuator/prometheus");
}

} 
