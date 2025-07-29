package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.service.UserService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * Fournit des endpoints pour récupérer, modifier et supprimer les profils utilisateurs.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * Service de gestion des utilisateurs.
     */
    private final UserService userService;

    /**
     * Récupère le profil de l'utilisateur authentifié.
     * L'utilisateur ne peut voir que son propre profil.
     *
     * @return ResponseEntity contenant le profil de l'utilisateur
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserProfileDTO profile = userService.getUserProfile(currentUserId);
        
        return ResponseEntity.ok(profile);
    }

    /**
     * Met à jour partiellement le profil de l'utilisateur authentifié.
     * L'utilisateur ne peut modifier que son propre profil.
     * Seuls les champs fournis sont modifiés.
     *
     * @param updateDTO les données de mise à jour du profil
     * @return ResponseEntity contenant le profil mis à jour
     */
    @PatchMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@RequestBody final UpdateUserDTO updateDTO) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserProfileDTO updatedProfile = userService.updateUserProfile(currentUserId, updateDTO);
        
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Supprime le compte de l'utilisateur authentifié.
     * Conformité RGPD : anonymisation par défaut des données personnelles.
     * L'utilisateur ne peut supprimer que son propre compte.
     *
     * @return ResponseEntity 204 (No Content) en cas de succès
     */
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUserAccount() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        userService.deleteUserAccount(currentUserId);
        
        return ResponseEntity.noContent().build();
    }
} 