package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceNotFoundExceptionTest {

    @Test
    @DisplayName("Doit construire l'exception avec le message fourni")
    void shouldCreateExceptionWithMessage() {
        // Arrange
        String message = "Le service demandé n'existe pas.";

        // Act
        ServiceNotFoundException exception = new ServiceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Le type doit bien être RuntimeException")
    void shouldBeRuntimeException() {
        // Act
        ServiceNotFoundException exception = new ServiceNotFoundException("test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}