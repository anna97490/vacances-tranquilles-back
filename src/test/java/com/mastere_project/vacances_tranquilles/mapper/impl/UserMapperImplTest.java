package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void toUser_WithRegisterClientDTO_ShouldReturnUser() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("Teste");
        dto.setLastName("Teste");
        dto.setEmail("teste@test.com");
        dto.setPassword("password123");
        dto.setPhoneNumber("0612345678");
        dto.setAddress("123 rue de Paris");
        dto.setCity("Toulouse");
        dto.setPostalCode("31000");
        User user = userMapper.toUser(dto);
        assertNotNull(user);
        assertEquals("Teste", user.getFirstName());
        assertEquals("Teste", user.getLastName());
        assertEquals("teste@test.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("0612345678", user.getPhoneNumber());
        assertEquals("123 rue de Paris", user.getAddress());
        assertEquals("Toulouse", user.getCity());
        assertEquals("31000", user.getPostalCode());
        assertEquals(UserRole.CLIENT, user.getUserRole());
        assertNull(user.getCompanyName());
        assertNull(user.getSiretSiren());
    }

    @Test
    void toUser_WithRegisterProviderDTO_ShouldReturnUser() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setFirstName("Anna");
        dto.setLastName("Cousin");
        dto.setEmail("anna@test.com");
        dto.setPassword("password123");
        dto.setPhoneNumber("0623456789");
        dto.setAddress("456 avenue de Lyon");
        dto.setCity("Lyon");
        dto.setPostalCode("69000");
        dto.setCompanyName("Sophie Services");
        dto.setSiretSiren("12340678900010");
        User user = userMapper.toUser(dto);
        assertNotNull(user);
        assertEquals("Anna", user.getFirstName());
        assertEquals("Cousin", user.getLastName());
        assertEquals("anna@test.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("0623456789", user.getPhoneNumber());
        assertEquals("456 avenue de Lyon", user.getAddress());
        assertEquals("Lyon", user.getCity());
        assertEquals("69000", user.getPostalCode());
        assertEquals(UserRole.PROVIDER, user.getUserRole());
        assertEquals("Sophie Services", user.getCompanyName());
        assertEquals("12340678900010", user.getSiretSiren());
    }

    @Test
    void toUserDTO_ShouldReturnUserDTO() {
        User user = new User();
        user.setId(37L);
        user.setFirstName("Teste");
        user.setLastName("Teste");
        user.setEmail("teste@test.com");
        user.setPhoneNumber("0612345678");
        user.setAddress("123 rue de Paris");
        user.setCity("Toulouse");
        user.setPostalCode("31000");
        user.setUserRole(UserRole.CLIENT);
        UserDTO dto = userMapper.toUserDTO(user);
        assertNotNull(dto);
        assertEquals(37L, dto.getId());
        assertEquals("Teste", dto.getFirstName());
        assertEquals("Teste", dto.getLastName());
        assertEquals("teste@test.com", dto.getEmail());
        assertEquals("0612345678", dto.getPhoneNumber());
        assertEquals("123 rue de Paris", dto.getAddress());
        assertEquals("Toulouse", dto.getCity());
        assertEquals("31000", dto.getPostalCode());
        assertEquals(UserRole.CLIENT, dto.getUserRole());
    }

    @Test
    void toUserProfileDTO_ShouldReturnUserProfileDTO() {
        User user = new User();
        user.setId(37L);
        user.setFirstName("Teste");
        user.setLastName("Teste");
        user.setEmail("teste@test.com");
        user.setUserRole(UserRole.CLIENT);
        user.setPhoneNumber("0612345678");
        user.setAddress("123 rue de Paris");
        user.setCity("Toulouse");
        user.setPostalCode("31000");
        user.setCompanyName("Test Company");
        user.setSiretSiren("12345678900000");
        UserProfileDTO dto = userMapper.toUserProfileDTO(user);
        assertNotNull(dto);
        assertEquals(37L, dto.getId());
        assertEquals("Teste", dto.getFirstName());
        assertEquals("Teste", dto.getLastName());
        assertEquals("teste@test.com", dto.getEmail());
        assertEquals(UserRole.CLIENT, dto.getUserRole());
        assertEquals("0612345678", dto.getPhoneNumber());
        assertEquals("123 rue de Paris", dto.getAddress());
        assertEquals("Toulouse", dto.getCity());
        assertEquals("31000", dto.getPostalCode());
        assertEquals("Test Company", dto.getCompanyName());
        assertEquals("12345678900000", dto.getSiretSiren());
    }

    @Test
    void updateUserFromDTO_WithAllFields_ShouldUpdateAllFields() {
        User user = new User();
        user.setFirstName("Ancien");
        user.setLastName("Nom");
        user.setPhoneNumber("0000000000");
        user.setAddress("Ancienne adresse");
        user.setCity("Ancienne ville");
        user.setPostalCode("00000");
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau");
        updateDTO.setLastName("Nom");
        updateDTO.setPhoneNumber("0612345678");
        updateDTO.setAddress("Nouvelle adresse");
        updateDTO.setCity("Nouvelle ville");
        updateDTO.setPostalCode("31000");
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        assertEquals("Nouveau", updatedUser.getFirstName());
        assertEquals("Nom", updatedUser.getLastName());
        assertEquals("0612345678", updatedUser.getPhoneNumber());
        assertEquals("Nouvelle adresse", updatedUser.getAddress());
        assertEquals("Nouvelle ville", updatedUser.getCity());
        assertEquals("31000", updatedUser.getPostalCode());
    }

    @Test
    void updateUserFromDTO_WithPartialFields_ShouldUpdateOnlyProvidedFields() {
        User user = new User();
        user.setFirstName("Ancien");
        user.setLastName("Nom");
        user.setPhoneNumber("0000000000");
        user.setAddress("Ancienne adresse");
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau");
        updateDTO.setPhoneNumber("0612345678");
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        assertEquals("Nouveau", updatedUser.getFirstName());
        assertEquals("Nom", updatedUser.getLastName());
        assertEquals("0612345678", updatedUser.getPhoneNumber());
        assertEquals("Ancienne adresse", updatedUser.getAddress());
    }

    @Test
    void updateUserFromDTO_WithNullFields_ShouldNotUpdateFields() {
        User user = new User();
        user.setFirstName("Teste");
        user.setLastName("Teste");
        user.setPhoneNumber("0612345678");
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName(null);
        updateDTO.setLastName(null);
        updateDTO.setPhoneNumber(null);
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        assertEquals("Teste", updatedUser.getFirstName());
        assertEquals("Teste", updatedUser.getLastName());
        assertEquals("0612345678", updatedUser.getPhoneNumber());
    }

    @Test
    void updateUserFromDTO_WithProviderFields_ShouldUpdateProviderFields() {
        User user = new User();
        user.setCompanyName("Ancienne société");
        user.setSiretSiren("00000000000000");
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setCompanyName("Nouvelle société");
        updateDTO.setSiretSiren("12345678900000");
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        assertEquals("Nouvelle société", updatedUser.getCompanyName());
        assertEquals("12345678900000", updatedUser.getSiretSiren());
    }


} 