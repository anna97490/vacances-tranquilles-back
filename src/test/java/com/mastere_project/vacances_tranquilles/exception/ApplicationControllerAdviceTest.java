package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.access.AccessDeniedException;

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
    @DisplayName("handleIllegalArgumentException should return 400 and error entity")
    void handleIllegalArgumentException_shouldReturn400() {
        IllegalArgumentException ex = new IllegalArgumentException("Note invalide");
        ResponseEntity<ErrorEntity> response = advice.handleIllegalArgument(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_ARGUMENT");
        assertThat(response.getBody().getMessage()).isEqualTo("Note invalide");
    }

    @Test
    @DisplayName("handleAllExceptions should return 500 and error entity")
    void handleAllExceptions_shouldReturn500() {
        Exception ex = new Exception("Erreur inconnue");
        ResponseEntity<ErrorEntity> response = advice.handleAllExceptions(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INTERNAL_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Erreur inconnue");
    }

    @Test
    @DisplayName("handleReservationNotFound should return 404 and error entity")
    void handleReservationNotFound_shouldReturn404() {
        ReservationNotFoundException ex = new ReservationNotFoundException("Réservation introuvable");
        ResponseEntity<ErrorEntity> response = advice.handleReservationNotFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("RESERVATION_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Réservation introuvable");
    }

    @Test
    @DisplayName("handleUnauthorizedAccess should return 403 and error entity")
    void handleUnauthorizedAccess_shouldReturn403() {
        UnauthorizedReservationAccessException ex = new UnauthorizedReservationAccessException("Accès non autorisé");
        ResponseEntity<ErrorEntity> response = advice.handleUnauthorizedAccess(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("UNAUTHORIZED_ACCESS");
        assertThat(response.getBody().getMessage()).isEqualTo("Accès non autorisé");
    }

    @Test
    @DisplayName("handleMissingReservationData should return 400 and error entity")
    void handleMissingReservationData_shouldReturn400() {
        MissingReservationDataException ex = new MissingReservationDataException("Données de réservation manquantes");
        ResponseEntity<ErrorEntity> response = advice.handleMissingReservationData(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("MISSING_RESERVATION_DATA");
        assertThat(response.getBody().getMessage()).isEqualTo("Données de réservation manquantes");
    }

    @Test
    @DisplayName("handleInvalidReservationStatus should return 400 and error entity")
    void handleInvalidReservationStatus_shouldReturn400() {
        InvalidReservationStatusException ex = new InvalidReservationStatusException("Statut invalide");
        ResponseEntity<ErrorEntity> response = advice.handleInvalidReservationStatus(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_RESERVATION_STATUS");
        assertThat(response.getBody().getMessage()).isEqualTo("Statut invalide");
    }

    @Test
    @DisplayName("handleInvalidReservationStatusTransition should return 400 and error entity")
    void handleInvalidReservationStatusTransition_shouldReturn400() {
        InvalidReservationStatusTransitionException ex = new InvalidReservationStatusTransitionException("Transition invalide");
        ResponseEntity<ErrorEntity> response = advice.handleInvalidReservationStatusTransition(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_RESERVATION_STATUS_TRANSITION");
        assertThat(response.getBody().getMessage()).isEqualTo("Transition invalide");
    }

    @Test
    @DisplayName("handleServiceNotFound should return 404 and error entity")
    void handleServiceNotFound_shouldReturn404() {
        ServiceNotFoundException ex = new ServiceNotFoundException("Service introuvable");
        ResponseEntity<ErrorEntity> response = advice.handleServiceNotFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("SERVICE_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Service introuvable");
    }

    @Test
    @DisplayName("handleUserNotFound should return 404 and error entity")
    void handleUserNotFound_shouldReturn404() {
        UserNotFoundException ex = new UserNotFoundException("Utilisateur introuvable");
        ResponseEntity<ErrorEntity> response = advice.handleUserNotFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Utilisateur introuvable");
    }

    @Test
    @DisplayName("handleAccessDenied should return 403 and error entity")
    void handleAccessDenied_shouldReturn403() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");
        ResponseEntity<ErrorEntity> response = advice.handleAccessDenied(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ACCESS_DENIED");
        assertThat(response.getBody().getMessage()).isEqualTo("Access denied");
    }

    @Test
    @DisplayName("handleInvalidToken should return 401 and error entity")
    void handleInvalidToken_shouldReturn401() {
        InvalidTokenException ex = new InvalidTokenException("Invalid token");
        ResponseEntity<ErrorEntity> response = advice.handleInvalidToken(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_TOKEN");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid token");
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
        ConversationForbiddenException ex = new ConversationForbiddenException("Conversation forbidden");
        ResponseEntity<ErrorEntity> response = advice.handleConversationForbidden(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("CONVERSATION_FORBIDDEN");
        assertThat(response.getBody().getMessage()).isEqualTo("Conversation forbidden");
    }

    @Test
    @DisplayName("handleConversationAlreadyExists should return 409 and error entity")
    void handleConversationAlreadyExists_shouldReturn409() {
        ConversationAlreadyExistsException ex = new ConversationAlreadyExistsException("Conversation already exists");
        ResponseEntity<ErrorEntity> response = advice.handleConversationAlreadyExists(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("CONVERSATION_ALREADY_EXISTS");
        assertThat(response.getBody().getMessage()).isEqualTo("Conversation already exists");
    }
}