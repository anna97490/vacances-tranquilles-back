package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ReservationNotFoundExceptionTest {

    @Test
    @DisplayName("Constructor should set message correctly")
    void constructor_shouldSetMessage() {
        String message = "Réservation introuvable";
        ReservationNotFoundException ex = new ReservationNotFoundException(message);
        assertThat(ex.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Exception should be instance of RuntimeException")
    void exception_shouldBeInstanceOfRuntimeException() {
        ReservationNotFoundException ex = new ReservationNotFoundException("Test message");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    @DisplayName("Constructor should handle empty message")
    void constructor_shouldHandleEmptyMessage() {
        ReservationNotFoundException ex = new ReservationNotFoundException("");
        assertThat(ex.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Constructor should handle null message")
    void constructor_shouldHandleNullMessage() {
        ReservationNotFoundException ex = new ReservationNotFoundException(null);
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    @DisplayName("Exception should have correct inheritance hierarchy")
    void exception_shouldHaveCorrectInheritanceHierarchy() {
        ReservationNotFoundException ex = new ReservationNotFoundException("Test");
        
        assertTrue(ex instanceof RuntimeException);
        assertTrue(ex instanceof Exception);
        assertTrue(ex instanceof Throwable);
    }

    @Test
    @DisplayName("Exception should be throwable")
    void exception_shouldBeThrowable() {
        String message = "Réservation avec ID 123 introuvable";
        ReservationNotFoundException ex = new ReservationNotFoundException(message);
        
        assertThatThrownBy(() -> {
            throw ex;
        }).isInstanceOf(ReservationNotFoundException.class)
          .hasMessage(message);
    }
} 