package com.mastere_project.vacances_tranquilles.service;

import java.util.Map;

import com.mastere_project.vacances_tranquilles.dto.StripeCheckoutSessionRequestDTO;

public interface StripeService {
    Map<String, String> createCheckoutSession(StripeCheckoutSessionRequestDTO dto);
}

