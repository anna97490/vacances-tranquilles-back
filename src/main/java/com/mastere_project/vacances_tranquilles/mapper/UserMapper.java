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

    /**
     * Convertit une entité User en DTO UserProfileDTO.
     * 
     * @param user l'entité User
     * @return le DTO profil correspondant
     */
    UserProfileDTO toUserProfileDTO(User user);

    /**
     * Met à jour une entité User avec les données du DTO de mise à jour.
     * Seuls les champs non-null du DTO sont mis à jour.
     * 
     * @param user l'entité User à mettre à jour
     * @param updateDTO le DTO contenant les nouvelles données
     * @return l'entité User mise à jour
     */
    User updateUserFromDTO(User user, UpdateUserDTO updateDTO);
}
