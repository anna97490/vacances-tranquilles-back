package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.LoginResponseDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.dto.UserDTO;
import com.mastere_project.vacances_tranquilles.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur d'authentification gérant l'inscription et la connexion des
 * utilisateurs.
 * Fournit des endpoints pour l'inscription client, l'inscription prestataire et
 * la connexion.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Inscription d'un nouveau client.
     * 
     * @param registerClientDTO Données du client à enregistrer
     * @return Réponse HTTP 200 si succès
     */
    @PostMapping("/register/client")
    public ResponseEntity<String> registerClient(@Valid @RequestBody RegisterClientDTO registerClientDTO) {
        userService.registerClient(registerClientDTO);
        return ResponseEntity.ok("Client registered successfully");
    }

    /**
     * Inscription d'un nouveau prestataire.
     * 
     * @param registerProviderDTO Données du prestataire à enregistrer
     * @return Réponse HTTP 200 si succès
     */
    @PostMapping("/register/provider")
    public ResponseEntity<String> registerProvider(@Valid @RequestBody RegisterProviderDTO registerProviderDTO) {
        userService.registerProvider(registerProviderDTO);
        return ResponseEntity.ok("Provider registered successfully");
    }

    /**
     * Connexion d'un utilisateur (client ou prestataire).
     * 
     * @param userDTO Données de connexion (email, mot de passe)
     * @return Token JWT et rôle utilisateur si succès
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO userDTO) {
        LoginResponseDTO response = userService.login(userDTO);
        return ResponseEntity.ok(response);
    }
}