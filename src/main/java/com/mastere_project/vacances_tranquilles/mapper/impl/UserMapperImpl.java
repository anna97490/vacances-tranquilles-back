package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.springframework.stereotype.Component;

/**
 * Implémentation du mapper UserMapper pour la conversion entre entités User et DTOs.
 */
@Component
public class UserMapperImpl implements UserMapper {

    /**
     * Convertit un RegisterClientDTO en entité User.
     * 
     * @param dto le DTO de registration client
     * @return l'entité User correspondante
     */
    @Override
    public User toUser(final RegisterClientDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setPostalCode(dto.getPostalCode());
        user.setCity(dto.getCity());
        user.setUserRole(UserRole.CLIENT);
        
        return user;
    }

    /**
     * Convertit un RegisterProviderDTO en entité User.
     * 
     * @param dto le DTO de registration prestataire
     * @return l'entité User correspondante
     */
    @Override
    public User toUser(final RegisterProviderDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setPostalCode(dto.getPostalCode());
        user.setCity(dto.getCity());
        user.setUserRole(UserRole.PROVIDER);
        user.setCompanyName(dto.getCompanyName());
        user.setSiretSiren(dto.getSiretSiren());
        
        return user;
    }

    /**
     * Convertit une entité User en UserDTO.
     * 
     * @param user l'entité User à convertir
     * @return le UserDTO correspondant
     */
    @Override
    public UserDTO toUserDTO(final User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setPostalCode(user.getPostalCode());
        dto.setCity(user.getCity());
        dto.setUserRole(user.getUserRole());
        
        return dto;
    }

    /**
     * Convertit une entité User en UserProfileDTO.
     * 
     * @param user l'entité User à convertir
     * @return le UserProfileDTO correspondant
     */
    @Override
    public UserProfileDTO toUserProfileDTO(final User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setPostalCode(user.getPostalCode());
        dto.setSiretSiren(user.getSiretSiren());
        dto.setCompanyName(user.getCompanyName());
        
        return dto;
    }

    /**
     * Met à jour une entité User à partir d'un UpdateUserDTO.
     * Seuls les champs non-null du DTO sont mis à jour.
     * 
     * @param user l'entité User à mettre à jour
     * @param updateDTO le DTO contenant les modifications
     * @return l'entité User mise à jour
     */
    @Override
    public User updateUserFromDTO(final User user, final UpdateUserDTO updateDTO) {
        if (updateDTO.getFirstName() != null) {
            user.setFirstName(updateDTO.getFirstName());
        }

        if (updateDTO.getLastName() != null) {
            user.setLastName(updateDTO.getLastName());
        }

        if (updateDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateDTO.getPhoneNumber());
        }

        if (updateDTO.getAddress() != null) {
            user.setAddress(updateDTO.getAddress());
        }
        
        if (updateDTO.getCity() != null) {
            user.setCity(updateDTO.getCity());
        }

        if (updateDTO.getPostalCode() != null) {
            user.setPostalCode(updateDTO.getPostalCode());
        }

        if (updateDTO.getSiretSiren() != null) {
            user.setSiretSiren(updateDTO.getSiretSiren());
        }

        if (updateDTO.getCompanyName() != null) {
            user.setCompanyName(updateDTO.getCompanyName());
        }
        
        return user;
    }

    /**
     * Convertit une entité User en UserBasicInfoDTO.
     * Ne retourne que le nom et prénom pour la confidentialité.
     * 
     * @param user l'entité User à convertir
     * @return le UserBasicInfoDTO correspondant
     */
    @Override
    public UserBasicInfoDTO toUserBasicInfoDTO(final User user) {
        UserBasicInfoDTO dto = new UserBasicInfoDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        
        return dto;
    }
}
