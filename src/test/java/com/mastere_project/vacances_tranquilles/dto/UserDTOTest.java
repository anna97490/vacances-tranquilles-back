package com.mastere_project.vacances_tranquilles.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john@doe.com");
        user1.setPassword("pass");
        user1.setUserRole(com.mastere_project.vacances_tranquilles.model.enums.UserRole.CLIENT);
        user1.setPhoneNumber("1234567890");
        user1.setAddress("1 rue de Paris");
        user1.setCity("Paris");
        user1.setPostalCode("75000");
        user1.setSiretSiren("123456789");
        user1.setCompanyName("Company");
        assertEquals(1L, user1.getId());
        assertEquals("John", user1.getFirstName());
        assertEquals("Doe", user1.getLastName());
        assertEquals("john@doe.com", user1.getEmail());
        assertEquals("pass", user1.getPassword());
        assertEquals(com.mastere_project.vacances_tranquilles.model.enums.UserRole.CLIENT, user1.getUserRole());
        assertEquals("1234567890", user1.getPhoneNumber());
        assertEquals("1 rue de Paris", user1.getAddress());
        assertEquals("Paris", user1.getCity());
        assertEquals("75000", user1.getPostalCode());
        assertEquals("123456789", user1.getSiretSiren());
        assertEquals("Company", user1.getCompanyName());
        // equals/hashCode
        UserDTO user2 = new UserDTO();
        user2.setId(1L);
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setEmail("john@doe.com");
        user2.setPassword("pass");
        user2.setUserRole(com.mastere_project.vacances_tranquilles.model.enums.UserRole.CLIENT);
        user2.setPhoneNumber("1234567890");
        user2.setAddress("1 rue de Paris");
        user2.setCity("Paris");
        user2.setPostalCode("75000");
        user2.setSiretSiren("123456789");
        user2.setCompanyName("Company");
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertTrue(user1.toString().contains("John"));
    }
} 