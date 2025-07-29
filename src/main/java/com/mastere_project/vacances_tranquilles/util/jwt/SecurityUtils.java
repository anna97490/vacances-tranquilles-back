package com.mastere_project.vacances_tranquilles.util.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Classe utilitaire pour la sécurité et l'accès à l'utilisateur courant.
 */
public final class SecurityUtils {
    private SecurityUtils() {
    }

    /**
     * Récupère l'identifiant de l'utilisateur actuellement authentifié.
     * 
     * @return l'identifiant utilisateur
     * @throws AccessDeniedException si l'utilisateur n'est pas authentifié
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new AccessDeniedException("Utilisateur non authentifié !");
        }
        return userId;
    }
}