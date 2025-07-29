package com.mastere_project.vacances_tranquilles.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.mastere_project.vacances_tranquilles.dto.StripeCheckoutSessionRequestDTO;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.service.StripeService;
import com.mastere_project.vacances_tranquilles.entity.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;

@org.springframework.stereotype.Service
public class StripeServiceImpl implements StripeService {

    @Autowired
    private ServiceRepository serviceRepo;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Map<String, String> createCheckoutSession(StripeCheckoutSessionRequestDTO dto) {
        Service service = serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        Duration duration = Duration.between(dto.getStartTime(), dto.getEndTime());
        long hours = duration.toHours();

        if (hours <= 0) {
            throw new IllegalArgumentException("Invalid time range");
        }

        BigDecimal durationHours = BigDecimal.valueOf(hours);

        BigDecimal totalAmount = service.getPrice().multiply(durationHours);

        long totalAmountInCents = totalAmount.multiply(BigDecimal.valueOf(100)).longValueExact();

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendBaseUrl + "/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(frontendBaseUrl + "/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount(totalAmountInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(service.getTitle())
                                                                    .build())
                                                    .build())
                                    .build())
                    .putMetadata("serviceId", dto.getServiceId().toString())
                    .putMetadata("customerId", dto.getCustomerId().toString())
                    .putMetadata("providerId", dto.getProviderId().toString())
                    .putMetadata("date", dto.getDate().toString())
                    .putMetadata("startTime", dto.getStartTime().toString())
                    .putMetadata("endTime", dto.getEndTime().toString())
                    .build();

            Session session = Session.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("sessionId", session.getId());
            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Stripe session creation failed", e);
        }
    }
}
