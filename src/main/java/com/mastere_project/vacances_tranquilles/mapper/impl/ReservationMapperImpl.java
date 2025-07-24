package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.Payment;
import com.mastere_project.vacances_tranquilles.entity.Reservation;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.mapper.ReservationMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

/**
 * Implémentation du mapper pour la conversion entre entités Reservation et DTOs.
 * Gère la transformation des objets Reservation en ReservationDTO avec tous leurs
 * objets associés (client, prestataire, service, paiement).
 */
@Component
public class ReservationMapperImpl implements ReservationMapper {

    /**
     * Convertit une entité Reservation en ReservationDTO.
     * Mappe tous les champs de base et les objets associés (client, prestataire, service, paiement).
     * Gère les cas où les objets associés peuvent être null.
     *
     * @param reservation L'entité Reservation à convertir
     * @return Le ReservationDTO correspondant, ou null si reservation est null
     */
    @Override
    public ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setStatus(reservation.getStatus());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setComment(reservation.getComment());
        dto.setTotalPrice(reservation.getTotalPrice());

        // Mapper client
        User client = reservation.getClient();
        if (client != null) {
            SimpleUserDTO clientDto = new SimpleUserDTO();
            clientDto.setId(client.getId());
            clientDto.setFirstName(client.getFirstName());
            clientDto.setLastName(client.getLastName());
            clientDto.setUserRole(client.getUserRole().name());
            clientDto.setAddress(client.getAddress());
            clientDto.setCity(client.getCity());
            clientDto.setPostalCode(client.getPostalCode());
            dto.setClient(clientDto);
        }

        // Mapper provider
        User provider = reservation.getProvider();
        if (provider != null) {
            SimpleUserDTO providerDto = new SimpleUserDTO();
            providerDto.setId(provider.getId());
            providerDto.setFirstName(provider.getFirstName());
            providerDto.setLastName(provider.getLastName());
            providerDto.setUserRole(provider.getUserRole().name());
            providerDto.setAddress(provider.getAddress()); // ✅ ajouté
            providerDto.setCity(provider.getCity()); // ✅ ajouté
            providerDto.setPostalCode(provider.getPostalCode()); // ✅ ajouté
            dto.setProvider(providerDto);
        }

        // Mapper service
        Service service = reservation.getService();
        if (service != null) {
            SimpleServiceDTO serviceDto = new SimpleServiceDTO();
            serviceDto.setId(service.getId());
            serviceDto.setTitle(service.getTitle());
            serviceDto.setDescription(service.getDescription());
            serviceDto.setPrice(service.getPrice());
            if (service.getProvider() != null) {
                serviceDto.setProviderId(service.getProvider().getId());
            }
            dto.setService(serviceDto);
        }

        // Mapper payment
        Payment payment = reservation.getPayment();
        if (payment != null) {
            PaymentDTO paymentDto = new PaymentDTO();
            paymentDto.setId(payment.getId());
            paymentDto.setAmount(payment.getAmount());

            // Convertir LocalDateTime en java.util.Date
            if (payment.getPaymentDate() != null) {
                Date date = Date.from(payment.getPaymentDate().atZone(ZoneId.systemDefault()).toInstant());
                paymentDto.setPaymentDate(date);
            }

            paymentDto.setMethod(payment.getPaymentMethod());
            paymentDto.setStatus(payment.getStatus());

            dto.setPayment(paymentDto);
        }

        return dto;
    }
}
