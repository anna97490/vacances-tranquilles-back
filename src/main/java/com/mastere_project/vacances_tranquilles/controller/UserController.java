package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.DeleteAccountDTO;
import com.mastere_project.vacances_tranquilles.dto.UpdateUserDTO;
import com.mastere_project.vacances_tranquilles.dto.UserProfileDTO;
import com.mastere_project.vacances_tranquilles.exception.UserNotFoundException;
import com.mastere_project.vacances_tranquilles.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
     * Récupère tous les clients de la plateforme.
     * Accessible à tous les utilisateurs authentifiés.
     *
     * @return ResponseEntity contenant la liste des clients
     */
    @GetMapping("/clients")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserProfileDTO>> getAllClients() {
        List<UserProfileDTO> clients = userService.getAllClients();
        
        return ResponseEntity.ok(clients);
    }

    /**
     * Récupère tous les prestataires de la plateforme.
     * Accessible à tous les utilisateurs authentifiés.
     *
     * @return ResponseEntity contenant la liste des prestataires
     */
    @GetMapping("/providers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserProfileDTO>> getAllProviders() {
        List<UserProfileDTO> providers = userService.getAllProviders();
        
        return ResponseEntity.ok(providers);
    }

    /**
     * Récupère un client spécifique par son identifiant.
     * Accessible à tous les utilisateurs authentifiés.
     *
     * @param id L'identifiant du client à récupérer
     * @return ResponseEntity contenant le profil du client ou 404 si non trouvé
     */
    @GetMapping("/clients/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getClientById(@PathVariable Long id) {
        try {
            UserProfileDTO client = userService.getClientById(id);
            
            return ResponseEntity.ok(client);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère un prestataire spécifique par son identifiant.
     * Accessible à tous les utilisateurs authentifiés.
     *
     * @param id L'identifiant du prestataire à récupérer
     * @return ResponseEntity contenant le profil du prestataire ou 404 si non trouvé
     */
    @GetMapping("/providers/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getProviderById(@PathVariable Long id) {
        try {
            UserProfileDTO provider = userService.getProviderById(id);
            
            return ResponseEntity.ok(provider);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère le profil de l'utilisateur authentifié.
     * L'utilisateur ne peut voir que son propre profil.
     *
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant le profil de l'utilisateur
     */
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getUserProfile(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        
        try {
            UserProfileDTO profile = userService.getUserProfile(userId);
            
            return ResponseEntity.ok(profile);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Met à jour partiellement le profil de l'utilisateur authentifié.
     * L'utilisateur ne peut modifier que son propre profil.
     * Seuls les champs fournis sont modifiés.
     *
     * @param updateDTO Les données de mise à jour du profil
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity contenant le profil mis à jour
     */
    @PatchMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@RequestBody UpdateUserDTO updateDTO, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        
        try {
            UserProfileDTO updatedProfile = userService.updateUserProfile(userId, updateDTO);
            
            return ResponseEntity.ok(updatedProfile);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Supprime le compte de l'utilisateur authentifié.
     * Conformité RGPD : anonymisation par défaut des données personnelles.
     * L'utilisateur ne peut supprimer que son propre compte.
     *
     * @param deleteAccountDTO Les informations de suppression (raison, anonymisation)
     * @param principal L'objet principal contenant les informations de l'utilisateur authentifié
     * @return ResponseEntity 204 (No Content) en cas de succès
     */
    @DeleteMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUserAccount(@RequestBody(required = false) DeleteAccountDTO deleteAccountDTO, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        
        try {
            // Utiliser les valeurs par défaut si le DTO n'est pas fourni
            if (deleteAccountDTO == null) {
                deleteAccountDTO = new DeleteAccountDTO();
                deleteAccountDTO.setReason("Demande utilisateur");
            }
            
            userService.deleteUserAccount(userId, deleteAccountDTO);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 