package com.mastere_project.vacances_tranquilles.dto;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDTOTest {
    
    @Test
    void testGettersSettersEqualsHashCodeToString() {
        LoginResponseDTO l1 = new LoginResponseDTO("tok", UserRole.CLIENT);

        assertEquals("tok", l1.getToken());
        assertEquals(UserRole.CLIENT, l1.getUserRole());
        
        LoginResponseDTO l2 = new LoginResponseDTO("tok", UserRole.CLIENT);

        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
        assertTrue(l1.toString().contains("tok"));
    }
} 