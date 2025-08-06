package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedReservationAccessExceptionTest {

    @Test
    @DisplayName("Constructor should set message correctly")
    void constructor_shouldSetMessage() {
        String message = "Accès non autorisé à la réservation";
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException(message);
        assertThat(ex.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Exception should be instance of RuntimeException")
    void exception_shouldBeInstanceOfRuntimeException() {
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException("Test message");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    @DisplayName("Constructor should handle empty message")
    void constructor_shouldHandleEmptyMessage() {
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException("");
        assertThat(ex.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Constructor should handle null message")
    void constructor_shouldHandleNullMessage() {
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException(null);
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    @DisplayName("Exception should have correct inheritance hierarchy")
    void exception_shouldHaveCorrectInheritanceHierarchy() {
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException("Test");
        
        // Vérifier que c'est bien une RuntimeException
        assertTrue(ex instanceof RuntimeException);
        
        // Vérifier que c'est bien une Exception
        assertTrue(ex instanceof Exception);
        
        // Vérifier que c'est bien un Throwable
        assertTrue(ex instanceof Throwable);
    }

    @Test
    @DisplayName("Exception should be throwable")
    void exception_shouldBeThrowable() {
        String message = "L'utilisateur n'a pas les droits pour accéder à cette réservation";
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException(message);
        
        // Vérifier que l'exception peut être levée
        assertThatThrownBy(() -> {
            throw ex;
        }).isInstanceOf(UnauthorizedReservationAccessException.class)
          .hasMessage(message);
    }

    @Test
    @DisplayName("Exception should handle specific access control messages")
    void exception_shouldHandleSpecificAccessControlMessages() {
        String message = "L'utilisateur ID 123 ne peut pas accéder à la réservation ID 456";
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException(message);
        
        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex.getMessage()).contains("ID 123");
        assertThat(ex.getMessage()).contains("ID 456");
    }
} 