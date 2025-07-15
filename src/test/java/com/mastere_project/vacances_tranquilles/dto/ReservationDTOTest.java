package com.mastere_project.vacances_tranquilles.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReservationDTOTest {

    @Test
    void testGettersSettersEqualsHashCodeToString() {
        ReservationDTO r1 = new ReservationDTO();
        r1.setId(1L);
        r1.setStatus("CONFIRMED");
        r1.setReservationDate(LocalDateTime.now());
        r1.setComment("Test");
        r1.setStartDate(LocalDateTime.now());
        r1.setEndDate(LocalDateTime.now().plusDays(1));
        r1.setTotalPrice(100.0);

        assertEquals(1L, r1.getId());
        assertEquals("CONFIRMED", r1.getStatus());
        assertEquals("Test", r1.getComment());
        assertEquals(100.0, r1.getTotalPrice());

        ReservationDTO r2 = new ReservationDTO();
        r2.setId(1L);
        r2.setStatus("CONFIRMED");
        r2.setReservationDate(r1.getReservationDate());
        r2.setComment("Test");
        r2.setStartDate(r1.getStartDate());
        r2.setEndDate(r1.getEndDate());
        r2.setTotalPrice(100.0);
        
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertTrue(r1.toString().contains("CONFIRMED"));
    }
} 