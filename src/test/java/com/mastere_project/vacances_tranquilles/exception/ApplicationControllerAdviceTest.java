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

    @Test
    @DisplayName("handleAccountLockedException should return 423 and error entity")
    void handleAccountLockedException_shouldReturn423() {
        AccountLockedException ex = new AccountLockedException("Compte bloqué");
        ResponseEntity<ErrorEntity> response = advice.handleAccountLockedException(ex);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ACCOUNT_LOCKED");
        assertThat(response.getBody().getMessage()).isEqualTo("Compte bloqué");
    }

    @Test
    @DisplayName("handleLoginInternalException should return 500 and error entity")
    void handleLoginInternalException_shouldReturn500() {
        LoginInternalException ex = new LoginInternalException("Erreur interne", new RuntimeException());
        ResponseEntity<ErrorEntity> response = advice.handleLoginInternalException(ex);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("LOGIN_INTERNAL_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Erreur interne");
    }

    @Test
    @DisplayName("handleEmailNotFoundException should return 401 and error entity")
    void handleEmailNotFoundException_shouldReturn401() {
        EmailNotFoundException ex = new EmailNotFoundException("Email inconnu");
        ResponseEntity<ErrorEntity> response = advice.handleEmailNotFoundException(ex);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("EMAIL_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Email inconnu");
    }

    @Test
    @DisplayName("handleWrongPasswordException should return 401 and error entity")
    void handleWrongPasswordException_shouldReturn401() {
        WrongPasswordException ex = new WrongPasswordException("Mot de passe incorrect");
        ResponseEntity<ErrorEntity> response = advice.handleWrongPasswordException(ex);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("WRONG_PASSWORD");
        assertThat(response.getBody().getMessage()).isEqualTo("Mot de passe incorrect");
    }

    @Test
    @DisplayName("handleUnexpectedLoginException should return 500 and error entity")
    void handleUnexpectedLoginException_shouldReturn500() {
        UnexpectedLoginException ex = new UnexpectedLoginException("Erreur inattendue", new RuntimeException());
        ResponseEntity<ErrorEntity> response = advice.handleUnexpectedLoginException(ex);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("UNEXPECTED_LOGIN_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Erreur inattendue");
    }
}