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

    // Tests pour les cas d'erreur manquants

    @Test
    void toUser_WithNullRegisterClientDTO_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> userMapper.toUser((RegisterClientDTO) null));
    }

    @Test
    void toUser_WithNullRegisterProviderDTO_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> userMapper.toUser((RegisterProviderDTO) null));
    }

    @Test
    void toUserDTO_WithNullUser_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> userMapper.toUserDTO(null));
    }

    @Test
    void toUserProfileDTO_WithNullUser_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> userMapper.toUserProfileDTO(null));
    }

    @Test
    void updateUserFromDTO_WithNullUser_ShouldThrowException() {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Test");
        
        // Le code actuel ne vérifie pas si user est null, donc on s'attend à une NullPointerException
        // quand on essaie d'accéder aux propriétés de user
        assertThrows(NullPointerException.class, () -> userMapper.updateUserFromDTO(null, updateDTO));
    }

    @Test
    void updateUserFromDTO_WithNullUpdateDTO_ShouldThrowException() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> userMapper.updateUserFromDTO(user, null));
    }

    @Test
    void toUserBasicInfoDTO_WithNullUser_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> userMapper.toUserBasicInfoDTO(null));
    }

    // Tests pour les conversions avec des données null

    @Test
    void toUser_WithNullFieldsInRegisterClientDTO_ShouldHandleGracefully() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName(null);
        dto.setLastName(null);
        dto.setEmail(null);
        dto.setPassword(null);
        dto.setPhoneNumber(null);
        dto.setAddress(null);
        dto.setCity(null);
        dto.setPostalCode(null);
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getPhoneNumber());
        assertNull(user.getAddress());
        assertNull(user.getCity());
        assertNull(user.getPostalCode());
        assertEquals(UserRole.CLIENT, user.getUserRole());
    }

    @Test
    void toUser_WithNullFieldsInRegisterProviderDTO_ShouldHandleGracefully() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setFirstName(null);
        dto.setLastName(null);
        dto.setEmail(null);
        dto.setPassword(null);
        dto.setPhoneNumber(null);
        dto.setAddress(null);
        dto.setCity(null);
        dto.setPostalCode(null);
        dto.setCompanyName(null);
        dto.setSiretSiren(null);
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getPhoneNumber());
        assertNull(user.getAddress());
        assertNull(user.getCity());
        assertNull(user.getPostalCode());
        assertNull(user.getCompanyName());
        assertNull(user.getSiretSiren());
        assertEquals(UserRole.PROVIDER, user.getUserRole());
    }

    @Test
    void toUserDTO_WithNullFieldsInUser_ShouldHandleGracefully() {
        User user = new User();
        user.setId(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setEmail(null);
        user.setPhoneNumber(null);
        user.setAddress(null);
        user.setCity(null);
        user.setPostalCode(null);
        user.setUserRole(null);
        
        UserDTO dto = userMapper.toUserDTO(user);
        
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getEmail());
        assertNull(dto.getPhoneNumber());
        assertNull(dto.getAddress());
        assertNull(dto.getCity());
        assertNull(dto.getPostalCode());
        assertNull(dto.getUserRole());
    }

    @Test
    void toUserProfileDTO_WithNullFieldsInUser_ShouldHandleGracefully() {
        User user = new User();
        user.setId(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setEmail(null);
        user.setUserRole(null);
        user.setPhoneNumber(null);
        user.setAddress(null);
        user.setCity(null);
        user.setPostalCode(null);
        user.setSiretSiren(null);
        user.setCompanyName(null);
        
        UserProfileDTO dto = userMapper.toUserProfileDTO(user);
        
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getEmail());
        assertNull(dto.getUserRole());
        assertNull(dto.getPhoneNumber());
        assertNull(dto.getAddress());
        assertNull(dto.getCity());
        assertNull(dto.getPostalCode());
        assertNull(dto.getSiretSiren());
        assertNull(dto.getCompanyName());
    }

    @Test
    void toUserBasicInfoDTO_WithNullFieldsInUser_ShouldHandleGracefully() {
        User user = new User();
        user.setFirstName(null);
        user.setLastName(null);
        
        UserBasicInfoDTO dto = userMapper.toUserBasicInfoDTO(user);
        
        assertNotNull(dto);
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
    }

    // Tests pour les cas limites dans les mappings

    @Test
    void toUser_WithEmptyStringsInRegisterClientDTO_ShouldHandleGracefully() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("");
        dto.setLastName("");
        dto.setEmail("");
        dto.setPassword("");
        dto.setPhoneNumber("");
        dto.setAddress("");
        dto.setCity("");
        dto.setPostalCode("");
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());
        assertEquals("", user.getPhoneNumber());
        assertEquals("", user.getAddress());
        assertEquals("", user.getCity());
        assertEquals("", user.getPostalCode());
        assertEquals(UserRole.CLIENT, user.getUserRole());
    }

    @Test
    void toUser_WithEmptyStringsInRegisterProviderDTO_ShouldHandleGracefully() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setFirstName("");
        dto.setLastName("");
        dto.setEmail("");
        dto.setPassword("");
        dto.setPhoneNumber("");
        dto.setAddress("");
        dto.setCity("");
        dto.setPostalCode("");
        dto.setCompanyName("");
        dto.setSiretSiren("");
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());
        assertEquals("", user.getPhoneNumber());
        assertEquals("", user.getAddress());
        assertEquals("", user.getCity());
        assertEquals("", user.getPostalCode());
        assertEquals("", user.getCompanyName());
        assertEquals("", user.getSiretSiren());
        assertEquals(UserRole.PROVIDER, user.getUserRole());
    }

    @Test
    void updateUserFromDTO_WithEmptyStrings_ShouldUpdateFields() {
        User user = new User();
        user.setFirstName("Ancien");
        user.setLastName("Nom");
        user.setPhoneNumber("0000000000");
        user.setAddress("Ancienne adresse");
        user.setCity("Ancienne ville");
        user.setPostalCode("00000");
        user.setCompanyName("Ancienne société");
        user.setSiretSiren("00000000000000");
        
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("");
        updateDTO.setLastName("");
        updateDTO.setPhoneNumber("");
        updateDTO.setAddress("");
        updateDTO.setCity("");
        updateDTO.setPostalCode("");
        updateDTO.setCompanyName("");
        updateDTO.setSiretSiren("");
        
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        
        assertEquals("", updatedUser.getFirstName());
        assertEquals("", updatedUser.getLastName());
        assertEquals("", updatedUser.getPhoneNumber());
        assertEquals("", updatedUser.getAddress());
        assertEquals("", updatedUser.getCity());
        assertEquals("", updatedUser.getPostalCode());
        assertEquals("", updatedUser.getCompanyName());
        assertEquals("", updatedUser.getSiretSiren());
    }

    @Test
    void updateUserFromDTO_WithMixedNullAndEmptyFields_ShouldHandleCorrectly() {
        User user = new User();
        user.setFirstName("Ancien");
        user.setLastName("Nom");
        user.setPhoneNumber("0000000000");
        user.setAddress("Ancienne adresse");
        
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau");
        updateDTO.setLastName(null);
        updateDTO.setPhoneNumber("");
        updateDTO.setAddress(null);
        
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        
        assertEquals("Nouveau", updatedUser.getFirstName());
        assertEquals("Nom", updatedUser.getLastName());
        assertEquals("", updatedUser.getPhoneNumber());
        assertEquals("Ancienne adresse", updatedUser.getAddress());
    }

    // Tests pour les cas limites de performance

    @Test
    void toUser_WithVeryLongStrings_ShouldHandleGracefully() {
        String longString = "A".repeat(1000);
        
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName(longString);
        dto.setLastName(longString);
        dto.setEmail(longString);
        dto.setPassword(longString);
        dto.setPhoneNumber(longString);
        dto.setAddress(longString);
        dto.setCity(longString);
        dto.setPostalCode(longString);
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertEquals(longString, user.getFirstName());
        assertEquals(longString, user.getLastName());
        assertEquals(longString, user.getEmail());
        assertEquals(longString, user.getPassword());
        assertEquals(longString, user.getPhoneNumber());
        assertEquals(longString, user.getAddress());
        assertEquals(longString, user.getCity());
        assertEquals(longString, user.getPostalCode());
    }

    @Test
    void toUserProfileDTO_WithVeryLongStrings_ShouldHandleGracefully() {
        String longString = "A".repeat(1000);
        
        User user = new User();
        user.setId(1L);
        user.setFirstName(longString);
        user.setLastName(longString);
        user.setEmail(longString);
        user.setPhoneNumber(longString);
        user.setAddress(longString);
        user.setCity(longString);
        user.setPostalCode(longString);
        user.setCompanyName(longString);
        user.setSiretSiren(longString);
        user.setUserRole(UserRole.PROVIDER);
        
        UserProfileDTO dto = userMapper.toUserProfileDTO(user);
        
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(longString, dto.getFirstName());
        assertEquals(longString, dto.getLastName());
        assertEquals(longString, dto.getEmail());
        assertEquals(longString, dto.getPhoneNumber());
        assertEquals(longString, dto.getAddress());
        assertEquals(longString, dto.getCity());
        assertEquals(longString, dto.getPostalCode());
        assertEquals(longString, dto.getCompanyName());
        assertEquals(longString, dto.getSiretSiren());
        assertEquals(UserRole.PROVIDER, dto.getUserRole());
    }

    // Tests pour les cas d'erreur de mapping

    @Test
    void toUser_WithSpecialCharacters_ShouldHandleGracefully() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("José María");
        dto.setLastName("O'Connor");
        dto.setEmail("test@example.com");
        dto.setPassword("p@ssw0rd!");
        dto.setPhoneNumber("+33 1 23 45 67 89");
        dto.setAddress("123 rue de l'Église, 2ème étage");
        dto.setCity("Saint-Étienne");
        dto.setPostalCode("42-000");
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertEquals("José María", user.getFirstName());
        assertEquals("O'Connor", user.getLastName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("p@ssw0rd!", user.getPassword());
        assertEquals("+33 1 23 45 67 89", user.getPhoneNumber());
        assertEquals("123 rue de l'Église, 2ème étage", user.getAddress());
        assertEquals("Saint-Étienne", user.getCity());
        assertEquals("42-000", user.getPostalCode());
    }

    @Test
    void toUserProfileDTO_WithSpecialCharacters_ShouldHandleGracefully() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("José María");
        user.setLastName("O'Connor");
        user.setEmail("test@example.com");
        user.setPhoneNumber("+33 1 23 45 67 89");
        user.setAddress("123 rue de l'Église, 2ème étage");
        user.setCity("Saint-Étienne");
        user.setPostalCode("42-000");
        user.setCompanyName("Société & Fils, SARL");
        user.setSiretSiren("123-456-789-00012");
        user.setUserRole(UserRole.PROVIDER);
        
        UserProfileDTO dto = userMapper.toUserProfileDTO(user);
        
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("José María", dto.getFirstName());
        assertEquals("O'Connor", dto.getLastName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("+33 1 23 45 67 89", dto.getPhoneNumber());
        assertEquals("123 rue de l'Église, 2ème étage", dto.getAddress());
        assertEquals("Saint-Étienne", dto.getCity());
        assertEquals("42-000", dto.getPostalCode());
        assertEquals("Société & Fils, SARL", dto.getCompanyName());
        assertEquals("123-456-789-00012", dto.getSiretSiren());
        assertEquals(UserRole.PROVIDER, dto.getUserRole());
    }

    // Tests pour les cas limites de validation

    @Test
    void updateUserFromDTO_WithOnlyOneField_ShouldUpdateOnlyThatField() {
        User user = new User();
        user.setFirstName("Ancien");
        user.setLastName("Nom");
        user.setPhoneNumber("0000000000");
        user.setAddress("Ancienne adresse");
        user.setCity("Ancienne ville");
        user.setPostalCode("00000");
        user.setCompanyName("Ancienne société");
        user.setSiretSiren("00000000000000");
        
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("Nouveau");
        // Tous les autres champs restent null
        
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        
        assertEquals("Nouveau", updatedUser.getFirstName());
        assertEquals("Nom", updatedUser.getLastName());
        assertEquals("0000000000", updatedUser.getPhoneNumber());
        assertEquals("Ancienne adresse", updatedUser.getAddress());
        assertEquals("Ancienne ville", updatedUser.getCity());
        assertEquals("00000", updatedUser.getPostalCode());
        assertEquals("Ancienne société", updatedUser.getCompanyName());
        assertEquals("00000000000000", updatedUser.getSiretSiren());
    }

    @Test
    void updateUserFromDTO_WithAllNullFields_ShouldNotChangeUser() {
        User user = new User();
        user.setFirstName("Teste");
        user.setLastName("Teste");
        user.setPhoneNumber("0612345678");
        user.setAddress("123 rue de Paris");
        user.setCity("Toulouse");
        user.setPostalCode("31000");
        user.setCompanyName("Test Company");
        user.setSiretSiren("12345678900000");
        
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        // Tous les champs sont null par défaut
        
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        
        assertEquals("Teste", updatedUser.getFirstName());
        assertEquals("Teste", updatedUser.getLastName());
        assertEquals("0612345678", updatedUser.getPhoneNumber());
        assertEquals("123 rue de Paris", updatedUser.getAddress());
        assertEquals("Toulouse", updatedUser.getCity());
        assertEquals("31000", updatedUser.getPostalCode());
        assertEquals("Test Company", updatedUser.getCompanyName());
        assertEquals("12345678900000", updatedUser.getSiretSiren());
    }

    // Tests pour les cas d'erreur de données

    @Test
    void toUserBasicInfoDTO_WithEmptyStrings_ShouldHandleGracefully() {
        User user = new User();
        user.setFirstName("");
        user.setLastName("");
        
        UserBasicInfoDTO dto = userMapper.toUserBasicInfoDTO(user);
        
        assertNotNull(dto);
        assertEquals("", dto.getFirstName());
        assertEquals("", dto.getLastName());
    }

    @Test
    void toUserDTO_WithEmptyStrings_ShouldHandleGracefully() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("");
        user.setLastName("");
        user.setEmail("");
        user.setPhoneNumber("");
        user.setAddress("");
        user.setCity("");
        user.setPostalCode("");
        user.setUserRole(UserRole.CLIENT);
        
        UserDTO dto = userMapper.toUserDTO(user);
        
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("", dto.getFirstName());
        assertEquals("", dto.getLastName());
        assertEquals("", dto.getEmail());
        assertEquals("", dto.getPhoneNumber());
        assertEquals("", dto.getAddress());
        assertEquals("", dto.getCity());
        assertEquals("", dto.getPostalCode());
        assertEquals(UserRole.CLIENT, dto.getUserRole());
    }

    // Tests pour les cas limites de business logic

    @Test
    void toUser_WithProviderDTO_ShouldSetCorrectRole() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setFirstName("Test");
        dto.setLastName("Provider");
        dto.setEmail("provider@test.com");
        dto.setPassword("password123");
        dto.setCompanyName("Test Company");
        dto.setSiretSiren("12345678900000");
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertEquals(UserRole.PROVIDER, user.getUserRole());
        assertEquals("Test Company", user.getCompanyName());
        assertEquals("12345678900000", user.getSiretSiren());
    }

    @Test
    void toUser_WithClientDTO_ShouldSetCorrectRole() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("Test");
        dto.setLastName("Client");
        dto.setEmail("client@test.com");
        dto.setPassword("password123");
        
        User user = userMapper.toUser(dto);
        
        assertNotNull(user);
        assertEquals(UserRole.CLIENT, user.getUserRole());
        assertNull(user.getCompanyName());
        assertNull(user.getSiretSiren());
    }

    // Tests pour les cas d'erreur de conversion

    @Test
    void toUserProfileDTO_WithAllNullFields_ShouldCreateEmptyDTO() {
        User user = new User();
        // Tous les champs sont null par défaut
        
        UserProfileDTO dto = userMapper.toUserProfileDTO(user);
        
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getEmail());
        assertNull(dto.getUserRole());
        assertNull(dto.getPhoneNumber());
        assertNull(dto.getAddress());
        assertNull(dto.getCity());
        assertNull(dto.getPostalCode());
        assertNull(dto.getSiretSiren());
        assertNull(dto.getCompanyName());
    }

    @Test
    void toUserBasicInfoDTO_WithAllNullFields_ShouldCreateEmptyDTO() {
        User user = new User();
        // Tous les champs sont null par défaut
        
        UserBasicInfoDTO dto = userMapper.toUserBasicInfoDTO(user);
        
        assertNotNull(dto);
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
    }



    @Test
    void updateUserFromDTO_WithUnicodeCharacters_ShouldHandleGracefully() {
        User user = new User();
        user.setFirstName("Ancien");
        user.setLastName("Nom");
        
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setFirstName("José María");
        updateDTO.setLastName("O'Connor");
        updateDTO.setPhoneNumber("+33 1 23 45 67 89");
        updateDTO.setAddress("123 rue de l'Église, 2ème étage");
        updateDTO.setCity("Saint-Étienne");
        updateDTO.setPostalCode("42-000");
        updateDTO.setCompanyName("Société & Fils, SARL");
        updateDTO.setSiretSiren("123-456-789-00012");
        
        User updatedUser = userMapper.updateUserFromDTO(user, updateDTO);
        
        assertEquals("José María", updatedUser.getFirstName());
        assertEquals("O'Connor", updatedUser.getLastName());
        assertEquals("+33 1 23 45 67 89", updatedUser.getPhoneNumber());
        assertEquals("123 rue de l'Église, 2ème étage", updatedUser.getAddress());
        assertEquals("Saint-Étienne", updatedUser.getCity());
        assertEquals("42-000", updatedUser.getPostalCode());
        assertEquals("Société & Fils, SARL", updatedUser.getCompanyName());
        assertEquals("123-456-789-00012", updatedUser.getSiretSiren());
    }


} 