package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.Service;

/**
 * Mapper pour la conversion entre ServiceDTO et Service entity.
 */
public interface ServiceMapper {

    /**
     * Convertit un ServiceDTO en entité Service.
     * 
     * @param serviceDTO le DTO à convertir
     * @return l'entité Service correspondante
     */
    Service toEntity(ServiceDTO serviceDTO);

    /**
     * Convertit une entité Service en ServiceDTO.
     * 
     * @param service l'entité à convertir
     * @return le DTO correspondant
     */
    ServiceDTO toDto(Service service);

}
