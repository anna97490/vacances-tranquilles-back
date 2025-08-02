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
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email already used");
        ResponseEntity<ErrorEntity> response = advice.handleEmailAlreadyExists(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("EMAIL_ALREADY_USED");
        assertThat(response.getBody().getMessage()).isEqualTo("Email already used");
    }

    @Test
    @DisplayName("handleMissingField should return 400 and error entity")
    void handleMissingField_shouldReturn400() {
        MissingFieldException ex = new MissingFieldException("Missing field");
        ResponseEntity<ErrorEntity> response = advice.handleMissingField(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("MISSING_REQUIRED_FIELD");
        assertThat(response.getBody().getMessage()).isEqualTo("Missing field");
    }

    @Test
    @DisplayName("handleValidationErrors should return 400 and error entity with joined messages")
    void handleValidationErrors_shouldReturn400() {
        // Mocker les champs d'erreur
        FieldError fieldError1 = new FieldError("object", "field1", "Error 1");
        FieldError fieldError2 = new FieldError("object", "field2", "Error 2");
        List<FieldError> fieldErrors = List.of(fieldError1, fieldError2);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorEntity> response = advice.handleValidationErrors(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getMessage()).contains("Error 1");
        assertThat(response.getBody().getMessage()).contains("Error 2");
    }

    @Test
    @DisplayName("handleIllegalArgument should return 400 and error entity")
    void handleIllegalArgument_shouldReturn400() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        ResponseEntity<ErrorEntity> response = advice.handleIllegalArgument(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_ARGUMENT");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid argument");
    }

    @Test
    @DisplayName("handleAccountLockedException should return 423 and error entity")
    void handleAccountLockedException_shouldReturn423() {
        AccountLockedException ex = new AccountLockedException("Account locked");
        ResponseEntity<ErrorEntity> response = advice.handleAccountLockedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(423));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ACCOUNT_LOCKED");
        assertThat(response.getBody().getMessage()).isEqualTo("Account locked");
    }

    @Test
    @DisplayName("handleLoginInternalException should return 500 and error entity")
    void handleLoginInternalException_shouldReturn500() {
        LoginInternalException ex = new LoginInternalException("Login internal error", new RuntimeException("Cause"));
        ResponseEntity<ErrorEntity> response = advice.handleLoginInternalException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("LOGIN_INTERNAL_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Login internal error");
    }

    @Test
    @DisplayName("handleEmailNotFoundException should return 401 and error entity")
    void handleEmailNotFoundException_shouldReturn401() {
        EmailNotFoundException ex = new EmailNotFoundException("Email not found");
        ResponseEntity<ErrorEntity> response = advice.handleEmailNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("EMAIL_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Email not found");
    }

    @Test
    @DisplayName("handleWrongPasswordException should return 401 and error entity")
    void handleWrongPasswordException_shouldReturn401() {
        WrongPasswordException ex = new WrongPasswordException("Wrong password");
        ResponseEntity<ErrorEntity> response = advice.handleWrongPasswordException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("WRONG_PASSWORD");
        assertThat(response.getBody().getMessage()).isEqualTo("Wrong password");
    }

    @Test
    @DisplayName("handleUnexpectedLoginException should return 500 and error entity")
    void handleUnexpectedLoginException_shouldReturn500() {
        UnexpectedLoginException ex = new UnexpectedLoginException("Unexpected login error", new RuntimeException("Cause"));
        ResponseEntity<ErrorEntity> response = advice.handleUnexpectedLoginException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("UNEXPECTED_LOGIN_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Unexpected login error");
    }

    @Test
    @DisplayName("handleServiceNotFound should return 404 and error entity")
    void handleServiceNotFound_shouldReturn404() {
        ServiceNotFoundException ex = new ServiceNotFoundException("Service not found");
        ResponseEntity<ErrorEntity> response = advice.handleServiceNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("SERVICE_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Service not found");
    }

    @Test
    @DisplayName("handleConversationNotFound should return 404 and error entity")
    void handleConversationNotFound_shouldReturn404() {
        ConversationNotFoundException ex = new ConversationNotFoundException("Conversation not found");
        ResponseEntity<ErrorEntity> response = advice.handleConversationNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("CONVERSATION_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Conversation not found");
    }

    @Test
    @DisplayName("handleConversationForbidden should return 403 and error entity")
    void handleConversationForbidden_shouldReturn403() {
        ConversationForbiddenException ex = new ConversationForbiddenException("Access forbidden");
        ResponseEntity<ErrorEntity> response = advice.handleConversationForbidden(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("FORBIDDEN");
        assertThat(response.getBody().getMessage()).isEqualTo("Access forbidden");
    }

    @Test
    @DisplayName("handleConversationAlreadyExists should return 400 and error entity")
    void handleConversationAlreadyExists_shouldReturn400() {
        ConversationAlreadyExistsException ex = new ConversationAlreadyExistsException("Conversation already exists");
        ResponseEntity<ErrorEntity> response = advice.handleConversationAlreadyExists(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("CONVERSATION_ALREADY_EXISTS");
        assertThat(response.getBody().getMessage()).isEqualTo("Conversation already exists");
    }

    @Test
    @DisplayName("handleUserNotFound should return 404 and error entity")
    void handleUserNotFound_shouldReturn404() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<ErrorEntity> response = advice.handleUserNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("User not found");
    }

    @Test
    @DisplayName("handleAllExceptions should return 500 and error entity")
    void handleAllExceptions_shouldReturn500() {
        Exception ex = new Exception("Internal error");
        ResponseEntity<ErrorEntity> response = advice.handleAllExceptions(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INTERNAL_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Internal error");
    }
}