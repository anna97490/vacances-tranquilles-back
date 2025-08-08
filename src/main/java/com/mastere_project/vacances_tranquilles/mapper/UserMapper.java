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

    /**
     * Convertit une entité User en DTO UserBasicInfoDTO.
     * Ne retourne que le nom et prénom pour la confidentialité.
     * 
     * @param user l'entité User
     * @return le DTO avec les informations de base (nom et prénom)
     */
    UserBasicInfoDTO toUserBasicInfoDTO(User user);
}
