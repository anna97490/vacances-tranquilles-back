package com.mastere_project.vacances_tranquilles.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastere_project.vacances_tranquilles.dto.ConfirmReservationRequestDTO;
import com.mastere_project.vacances_tranquilles.dto.StripeCheckoutSessionRequestDTO;
import com.mastere_project.vacances_tranquilles.service.StripeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody StripeCheckoutSessionRequestDTO dto) {
        Map<String, String> response = stripeService.createCheckoutSession(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm-reservation")
    public ResponseEntity<Void> confirmReservation(@RequestBody ConfirmReservationRequestDTO dto) {
    stripeService.confirmReservation(dto.getSessionId());
    return ResponseEntity.ok().build();
}

}
