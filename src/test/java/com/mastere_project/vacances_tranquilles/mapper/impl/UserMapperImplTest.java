package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.dto.UserDTO;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserMapperImplTest {
    private final UserMapperImpl mapper = new UserMapperImpl();

    @Test
    @DisplayName("toUser(RegisterClientDTO) should map all fields and set CLIENT role")
    void toUser_fromClientDTO_shouldMapFields() {
        RegisterClientDTO dto = new RegisterClientDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Martin");
        dto.setEmail("alice@example.com");
        dto.setPassword("pass");
        dto.setPhoneNumber("0600000000");
        dto.setAddress("1 rue de Paris");
        dto.setPostalCode("75000");
        dto.setCity("Paris");

        User user = mapper.toUser(dto);
        assertThat(user.getFirstName()).isEqualTo("Alice");
        assertThat(user.getLastName()).isEqualTo("Martin");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getPassword()).isEqualTo("pass");
        assertThat(user.getPhoneNumber()).isEqualTo("0600000000");
        assertThat(user.getAddress()).isEqualTo("1 rue de Paris");
        assertThat(user.getPostalCode()).isEqualTo("75000");
        assertThat(user.getCity()).isEqualTo("Paris");
        assertThat(user.getUserRole()).isEqualTo(UserRole.CLIENT);
    }

    @Test
    @DisplayName("toUser(RegisterProviderDTO) should map all fields and set PRESTATAIRE role")
    void toUser_fromProviderDTO_shouldMapFields() {
        RegisterProviderDTO dto = new RegisterProviderDTO();
        dto.setFirstName("Bob");
        dto.setLastName("Durand");
        dto.setEmail("bob@example.com");
        dto.setPassword("pass");
        dto.setPhoneNumber("0611111111");
        dto.setAddress("2 avenue de Lyon");
        dto.setPostalCode("69000");
        dto.setCity("Lyon");
        dto.setCompanyName("Durand SARL");
        dto.setSiretSiren("12345678900015");

        User user = mapper.toUser(dto);
        assertThat(user.getFirstName()).isEqualTo("Bob");
        assertThat(user.getLastName()).isEqualTo("Durand");
        assertThat(user.getEmail()).isEqualTo("bob@example.com");
        assertThat(user.getPassword()).isEqualTo("pass");
        assertThat(user.getPhoneNumber()).isEqualTo("0611111111");
        assertThat(user.getAddress()).isEqualTo("2 avenue de Lyon");
        assertThat(user.getPostalCode()).isEqualTo("69000");
        assertThat(user.getCity()).isEqualTo("Lyon");
        assertThat(user.getUserRole()).isEqualTo(UserRole.PROVIDER);
        assertThat(user.getCompanyName()).isEqualTo("Durand SARL");
        assertThat(user.getSiretSiren()).isEqualTo("12345678900015");
    }

    @Test
    @DisplayName("toUserDTO(User) should map all fields")
    void toUserDTO_shouldMapFields() {
        User user = new User();
        user.setId(42L);
        user.setFirstName("Charlie");
        user.setLastName("Dupont");
        user.setEmail("charlie@example.com");
        user.setPhoneNumber("0622222222");
        user.setAddress("3 place Bellecour");
        user.setPostalCode("69002");
        user.setCity("Lyon");
        user.setUserRole(UserRole.CLIENT);

        UserDTO dto = mapper.toUserDTO(user);
        assertThat(dto.getId()).isEqualTo(42L);
        assertThat(dto.getFirstName()).isEqualTo("Charlie");
        assertThat(dto.getLastName()).isEqualTo("Dupont");
        assertThat(dto.getEmail()).isEqualTo("charlie@example.com");
        assertThat(dto.getPhoneNumber()).isEqualTo("0622222222");
        assertThat(dto.getAddress()).isEqualTo("3 place Bellecour");
        assertThat(dto.getPostalCode()).isEqualTo("69002");
        assertThat(dto.getCity()).isEqualTo("Lyon");
        assertThat(dto.getUserRole()).isEqualTo(UserRole.CLIENT);
    }
} 