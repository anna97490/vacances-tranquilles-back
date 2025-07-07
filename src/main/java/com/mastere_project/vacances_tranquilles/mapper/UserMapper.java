package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;

/**
 * Mapper pour la conversion entre entités User et DTOs.
 */
public interface UserMapper {
    /**
     * Convertit un DTO client en entité User.
     * 
     * @param dto le DTO client
     * @return l'entité User correspondante
     */
    User toUser(RegisterClientDTO dto);

    /**
     * Convertit un DTO prestataire en entité User.
     * 
     * @param dto le DTO prestataire
     * @return l'entité User correspondante
     */
    User toUser(RegisterProviderDTO dto);

    /**
     * Convertit une entité User en DTO UserDTO.
     * 
     * @param user l'entité User
     * @return le DTO correspondant
     */
    UserDTO toUserDTO(User user);
}
