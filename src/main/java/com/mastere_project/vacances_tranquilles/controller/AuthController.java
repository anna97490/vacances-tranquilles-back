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
     * @param registerClientDTO les données du client à enregistrer
     * @return une réponse HTTP 200 si l'inscription est un succès
     */
    @PostMapping("/register/client")
    public ResponseEntity<String> registerClient(@Valid @RequestBody RegisterClientDTO registerClientDTO) {
        userService.registerClient(registerClientDTO);
        return ResponseEntity.ok("Client registered successfully");
    }


    /**
     * Inscription d'un nouveau prestataire.
     *
     * @param registerProviderDTO les données du prestataire à enregistrer
     * @return une réponse HTTP 200 si l'inscription est un succès
     */
    @PostMapping("/register/provider")
    public ResponseEntity<String> registerProvider(@Valid @RequestBody RegisterProviderDTO registerProviderDTO) {
        userService.registerProvider(registerProviderDTO);
        return ResponseEntity.ok("Provider registered successfully");
    }

    
    /**
     * Connexion d'un utilisateur (client ou prestataire).
     *
     * @param userDTO les données de connexion de l'utilisateur (email, mot de passe)
     * @return une réponse contenant le token JWT et le rôle de l'utilisateur si la connexion est un succès
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO userDTO) {
        LoginResponseDTO response = userService.login(userDTO);
        return ResponseEntity.ok(response);
    }
}