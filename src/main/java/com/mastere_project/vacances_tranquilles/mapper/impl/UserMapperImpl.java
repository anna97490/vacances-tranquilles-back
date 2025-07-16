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

    @Override
    public User toUser(RegisterClientDTO dto) {
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

    @Override
    public User toUser(RegisterProviderDTO dto) {
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

    @Override
    public UserDTO toUserDTO(User user) {
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
}
