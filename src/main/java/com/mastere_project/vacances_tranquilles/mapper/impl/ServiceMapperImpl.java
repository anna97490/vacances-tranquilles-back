package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.mapper.ServiceMapper;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Implémentation du mapper ServiceMapper pour la conversion entre Service et
 * ServiceDTO.
 * Permet de transformer les entités Service en DTO et inversement, en gérant la
 * récupération du provider via UserRepository.
 */
@Component
public class ServiceMapperImpl implements ServiceMapper {

    UserRepository userRepository;

    /**
     * Convertit une entité Service en ServiceDTO.
     *
     * @param service l'entité Service à convertir
     * @return le DTO correspondant, ou null si service est null
     */
    public ServiceDTO toDto(Service service) {
        if (service == null) {
            return null;
        }
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(service.getId());
        serviceDTO.setTitle(service.getTitle());
        serviceDTO.setDescription(service.getDescription());
        serviceDTO.setCategory(service.getCategory());
        serviceDTO.setPrice(service.getPrice());
        serviceDTO.setProviderId(service.getProvider() != null ? service.getProvider().getId() : null);
        return serviceDTO;
    }

    /**
     * Convertit un ServiceDTO en entité Service.
     *
     * @param serviceDTO le DTO à convertir
     * @return l'entité Service correspondante, ou null si serviceDTO est null
     * @throws RuntimeException si le provider référencé n'existe pas
     */
    public Service toEntity(ServiceDTO serviceDTO) {
        if (serviceDTO == null) {
            return null;
        }
        Service service = new Service();
        service.setId(serviceDTO.getId());
        service.setTitle(serviceDTO.getTitle());
        service.setDescription(serviceDTO.getDescription());
        service.setCategory(serviceDTO.getCategory());
        service.setPrice(serviceDTO.getPrice());
        if (serviceDTO.getProviderId() != null) {
            User provider = userRepository.findById(serviceDTO.getProviderId())
                    .orElseThrow(() -> new RuntimeException("Provider not found"));
            service.setProvider(provider);
        }

        return service;
    }
}
