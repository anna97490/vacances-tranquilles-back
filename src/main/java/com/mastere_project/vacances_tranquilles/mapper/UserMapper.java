package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;

/**
 * Mapper pour la conversion entre entités User et DTOs associés.
 */
public interface UserMapper {
    /**
     * Convertit un RegisterClientDTO en entité User.
     *
     * @param dto le DTO contenant les informations du client
     * @return l'entité User correspondante
     */
    User toUser(RegisterClientDTO dto);


    /**
     * Convertit un RegisterProviderDTO en entité User.
     *
     * @param dto le DTO contenant les informations du prestataire
     * @return l'entité User correspondante
     */
    User toUser(RegisterProviderDTO dto);

    
    /**
     * Convertit une entité User en UserDTO.
     *
     * @param user l'entité User à convertir
     * @return le DTO utilisateur correspondant
     */
    UserDTO toUserDTO(User user);
}
