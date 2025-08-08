package com.mastere_project.vacances_tranquilles.util.jwt;

import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
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

    /**
     * Récupère le rôle de l'utilisateur actuellement authentifié.
     * Le rôle est récupéré depuis la base de données pour éviter la manipulation côté client.
     * 
     * @param userRepository Repository pour accéder aux données utilisateur
     * @return le rôle de l'utilisateur
     * @throws AccessDeniedException si l'utilisateur n'est pas authentifié ou introuvable
     */
    public static UserRole getCurrentUserRole(UserRepository userRepository) {
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccessDeniedException("Utilisateur introuvable"));
        return user.getUserRole();
    }
}
