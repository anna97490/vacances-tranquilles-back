package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MissingReservationDataExceptionTest {

    @Test
    @DisplayName("Constructor should set message correctly")
    void constructor_shouldSetMessage() {
        String message = "Données de réservation manquantes";
        MissingReservationDataException ex = new MissingReservationDataException(message);
        assertThat(ex.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Exception should be instance of RuntimeException")
    void exception_shouldBeInstanceOfRuntimeException() {
        MissingReservationDataException ex = new MissingReservationDataException("Test message");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    @DisplayName("Constructor should handle empty message")
    void constructor_shouldHandleEmptyMessage() {
        MissingReservationDataException ex = new MissingReservationDataException("");
        assertThat(ex.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Constructor should handle null message")
    void constructor_shouldHandleNullMessage() {
        MissingReservationDataException ex = new MissingReservationDataException(null);
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    @DisplayName("Exception should have correct inheritance hierarchy")
    void exception_shouldHaveCorrectInheritanceHierarchy() {
        MissingReservationDataException ex = new MissingReservationDataException("Test");
        
        // Vérifier que c'est bien une RuntimeException
        assertTrue(ex instanceof RuntimeException);
        
        // Vérifier que c'est bien une Exception
        assertTrue(ex instanceof Exception);
        
        // Vérifier que c'est bien un Throwable
        assertTrue(ex instanceof Throwable);
    }
} 