package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody RegisterClientDTO registerClientDTO) {
        userService.registerClient(registerClientDTO);
        return ResponseEntity.ok("Client registered successfully");
    }

    @PostMapping("/register/provider")
    public ResponseEntity<?> registerProvider(@Valid @RequestBody RegisterProviderDTO registerProviderDTO) {
        userService.registerProvider(registerProviderDTO);
        return ResponseEntity.ok("Provider registered successfully");
    }
}
