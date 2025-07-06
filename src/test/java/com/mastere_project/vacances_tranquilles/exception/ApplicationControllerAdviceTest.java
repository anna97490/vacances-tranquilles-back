package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationControllerAdviceTest {
    private final ApplicationControllerAdvice advice = new ApplicationControllerAdvice();

    @Test
    @DisplayName("handleEmailAlreadyExists should return 409 and error entity")
    void handleEmailAlreadyExists_shouldReturn409() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email déjà utilisé");
        ResponseEntity<ErrorEntity> response = advice.handleEmailAlreadyExists(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("EMAIL_ALREADY_USED");
        assertThat(response.getBody().getMessage()).isEqualTo("Email déjà utilisé");
    }

    @Test
    @DisplayName("handleMissingField should return 400 and error entity")
    void handleMissingField_shouldReturn400() {
        MissingFieldException ex = new MissingFieldException("Champ manquant");
        ResponseEntity<ErrorEntity> response = advice.handleMissingField(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("MISSING_REQUIRED_FIELD");
        assertThat(response.getBody().getMessage()).isEqualTo("Champ manquant");
    }

    @Test
    @DisplayName("handleValidationErrors should return 400 and error entity with joined messages")
    void handleValidationErrors_shouldReturn400() {
        // Mock FieldErrors
        FieldError fieldError1 = new FieldError("object", "field1", "Erreur 1");
        FieldError fieldError2 = new FieldError("object", "field2", "Erreur 2");
        List<FieldError> fieldErrors = List.of(fieldError1, fieldError2);

        // Mock BindingResult
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Mock MethodArgumentNotValidException
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorEntity> response = advice.handleValidationErrors(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getMessage()).contains("Erreur 1");
        assertThat(response.getBody().getMessage()).contains("Erreur 2");
    }
} 