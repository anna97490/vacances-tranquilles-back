package com.mastere_project.vacances_tranquilles.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterClientDTOTest {

    @Test
    void testGettersSettersEqualsHashCodeToString() {
        RegisterClientDTO r1 = new RegisterClientDTO();
        r1.setFirstName("John");
        r1.setLastName("Doe");
        r1.setEmail("john@doe.com");
        r1.setPassword("pass");
        r1.setPhoneNumber("1234567890");
        r1.setAddress("1 rue de Paris");
        r1.setCity("Paris");
        r1.setPostalCode("75000");

        assertEquals("John", r1.getFirstName());
        assertEquals("Doe", r1.getLastName());
        assertEquals("john@doe.com", r1.getEmail());
        assertEquals("pass", r1.getPassword());
        assertEquals("1234567890", r1.getPhoneNumber());
        assertEquals("1 rue de Paris", r1.getAddress());
        assertEquals("Paris", r1.getCity());
        assertEquals("75000", r1.getPostalCode());

        RegisterClientDTO r2 = new RegisterClientDTO();
        r2.setFirstName("John");
        r2.setLastName("Doe");
        r2.setEmail("john@doe.com");
        r2.setPassword("pass");
        r2.setPhoneNumber("1234567890");
        r2.setAddress("1 rue de Paris");
        r2.setCity("Paris");
        r2.setPostalCode("75000");
        
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertTrue(r1.toString().contains("John"));
    }
} 