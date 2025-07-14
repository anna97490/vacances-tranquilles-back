package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountLockedExceptionTest {
    @Test
    void testMessage() {
        String message = "Compte bloqué temporairement.";
        AccountLockedException exception = new AccountLockedException(message);
        assertEquals(message, exception.getMessage());
    }
}