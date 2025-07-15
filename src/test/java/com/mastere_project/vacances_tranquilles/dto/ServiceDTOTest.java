package com.mastere_project.vacances_tranquilles.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceDTOTest {
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        ServiceDTO s1 = new ServiceDTO();
        s1.setId(1L);
        s1.setTitle("Service");
        s1.setDescription("Desc");
        s1.setPrice(99.99);

        assertEquals(1L, s1.getId());
        assertEquals("Service", s1.getTitle());
        assertEquals("Desc", s1.getDescription());
        assertEquals(99.99, s1.getPrice());

        ServiceDTO s2 = new ServiceDTO();
        s2.setId(1L);
        s2.setTitle("Service");
        s2.setDescription("Desc");
        s2.setPrice(99.99);
        
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertTrue(s1.toString().contains("Service"));
    }
} 