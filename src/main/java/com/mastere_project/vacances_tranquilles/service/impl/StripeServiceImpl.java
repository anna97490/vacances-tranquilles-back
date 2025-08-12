package com.mastere_project.vacances_tranquilles.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.mastere_project.vacances_tranquilles.dto.ReservationDTO;
import com.mastere_project.vacances_tranquilles.dto.StripeCheckoutSessionRequestDTO;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.service.ReservationService;
import com.mastere_project.vacances_tranquilles.service.StripeService;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.exception.ServiceNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.StripeSessionCreationException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private static final String SERVICE_NOT_FOUND_MSG = "Service not found";
    private final ServiceRepository serviceRepo;
    private final ReservationService reservationService;

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
                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MSG));

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
            throw new StripeSessionCreationException("La création de la session Stripe a échoué.", e);
        }
    }

    @Override
    public void confirmReservation(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            Map<String, String> metadata = session.getMetadata();

            Long serviceId = Long.parseLong(metadata.get("serviceId"));
            Long customerId = Long.parseLong(metadata.get("customerId"));
            Long providerId = Long.parseLong(metadata.get("providerId"));
            LocalDate date = LocalDate.parse(metadata.get("date"));
            LocalTime start = LocalTime.parse(metadata.get("startTime"));
            LocalTime end = LocalTime.parse(metadata.get("endTime"));

            Service service = serviceRepo.findById(serviceId)
                    .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MSG));

            Duration duration = Duration.between(start, end);
            long hours = duration.toHours();
            BigDecimal price = service.getPrice().multiply(BigDecimal.valueOf(hours));

            ReservationDTO dto = new ReservationDTO();
            dto.setServiceId(serviceId);
            dto.setClientId(customerId);
            dto.setProviderId(providerId);
            dto.setReservationDate(date.atStartOfDay());
            dto.setStartDate(date.atTime(start));
            dto.setEndDate(date.atTime(end));
            dto.setTotalPrice(price);

            reservationService.createReservation(dto);

        } catch (StripeException e) {
            throw new StripeSessionCreationException("La récupération de la session Stripe a échoué.", e);
        }
    }

}
