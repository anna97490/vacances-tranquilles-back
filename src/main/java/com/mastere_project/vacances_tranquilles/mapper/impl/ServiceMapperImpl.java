package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.mapper.ServiceMapper;
import org.springframework.stereotype.Component;

/**
 * Implémentation du mapper ServiceMapper pour la conversion entre Service et
 * ServiceDTO.
 * Permet de transformer les entités Service en DTO et inversement.
 * Note: Le provider est géré par le service métier, pas par le mapper.
 */
@Component
public class ServiceMapperImpl implements ServiceMapper {

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
     * Note: Le provider doit être défini séparément via setProvider() car il est géré par le service métier.
     *
     * @param serviceDTO le DTO à convertir
     * @return l'entité Service correspondante, ou null si serviceDTO est null
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
        // Le provider est géré par le service métier, pas par le mapper

        return service;
    }
}
