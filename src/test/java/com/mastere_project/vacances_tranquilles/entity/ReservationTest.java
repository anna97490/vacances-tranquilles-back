package com.mastere_project.vacances_tranquilles.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setStatus("CONFIRMED");
        assertEquals(1L, r1.getId());
        assertEquals("CONFIRMED", r1.getStatus());

        Reservation r2 = new Reservation();
        r2.setId(1L);
        r2.setStatus("CONFIRMED");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertTrue(r1.toString().contains("CONFIRMED"));
    }
} 