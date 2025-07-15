package com.mastere_project.vacances_tranquilles.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        Service s1 = new Service();
        s1.setId(1L);
        s1.setTitle("Service");
        s1.setPrice(99.99);
        assertEquals(1L, s1.getId());
        assertEquals("Service", s1.getTitle());
        assertEquals(99.99, s1.getPrice());

        Service s2 = new Service();
        s2.setId(1L);
        s2.setTitle("Service");
        s2.setPrice(99.99);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertTrue(s1.toString().contains("Service"));
    }
} 