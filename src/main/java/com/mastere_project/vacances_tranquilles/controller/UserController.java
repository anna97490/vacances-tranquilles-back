package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserBasicInfoDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * Fournit des endpoints pour récupérer, modifier et supprimer les profils
 * utilisateurs.
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
        UserProfileDTO profile = userService.getUserProfile();

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
        UserProfileDTO updatedProfile = userService.updateUserProfile(updateDTO);

        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Supprime le compte de l'utilisateur authentifié.
     * Conformité RGPD : anonymisation par défaut des données personnelles.
     * L'utilisateur ne peut supprimer que son propre compte.
     *
     * @return ResponseEntity avec message de confirmation
     */
    @DeleteMapping("/profile")
    public ResponseEntity<Object> deleteUserAccount() {
        userService.deleteUserAccount();

        return ResponseEntity.ok()
                .body(new Object() {
                    public final String message = "Account deletion completed. All personal data has been deleted or anonymized.";
                });
    }

    /**
     * Récupère les informations de base d'un utilisateur par son ID.
     * Cette route permet de récupérer seulement le nom et prénom d'un utilisateur.
     * L'accès est contrôlé par la sécurité Spring Security.
     *
     * @param userId l'identifiant de l'utilisateur à récupérer
     * @return ResponseEntity contenant les informations de base de l'utilisateur
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserBasicInfoDTO> getUserById(@PathVariable final Long userId) {
        UserBasicInfoDTO userBasicInfo = userService.getUserBasicInfoById(userId);
        
        return ResponseEntity.ok(userBasicInfo);
    }
}