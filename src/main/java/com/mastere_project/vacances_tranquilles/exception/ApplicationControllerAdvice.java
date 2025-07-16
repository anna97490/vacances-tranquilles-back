package com.mastere_project.vacances_tranquilles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gère les exceptions globales de l'application et fournit des réponses d'erreur formatées.
 */
@ControllerAdvice
public class ApplicationControllerAdvice {

    /**
     * Gère l'exception EmailAlreadyExistsException.
     * @param ex l'exception levée
     * @return une réponse HTTP 409 avec l'entité d'erreur
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorEntity error = new ErrorEntity("EMAIL_ALREADY_USED", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Gère l'exception MissingFieldException.
     * @param ex l'exception levée
     * @return une réponse HTTP 400 avec l'entité d'erreur
     */
    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<ErrorEntity> handleMissingField(MissingFieldException ex) {
        ErrorEntity error = new ErrorEntity("MISSING_REQUIRED_FIELD", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Gère les erreurs de validation des arguments de méthode.
     * @param ex l'exception levée
     * @return une réponse HTTP 400 avec l'entité d'erreur
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorEntity> handleValidationErrors(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("; "));
        ErrorEntity error = new ErrorEntity("VALIDATION_ERROR", sb.toString());
        return ResponseEntity.badRequest().body(error);
    }

  
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorEntity> handleAccountLockedException(AccountLockedException ex) {
        ErrorEntity error = new ErrorEntity("ACCOUNT_LOCKED", ex.getMessage());
        return ResponseEntity.status(423).body(error);
    }

    /**
     * Gère l'exception levée en cas d'erreur interne inattendue lors de la
     * connexion.
     *
     * @param ex l'exception LoginInternalException
     * @return une réponse HTTP 500 avec un code d'erreur spécifique
     */
    @ExceptionHandler(LoginInternalException.class)
    public ResponseEntity<ErrorEntity> handleLoginInternalException(LoginInternalException ex) {
        ErrorEntity error = new ErrorEntity("LOGIN_INTERNAL_ERROR", ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }

    /**
     * Gère l'exception levée lorsqu'aucun utilisateur n'est trouvé pour un email
     * donné.
     *
     * @param ex l'exception EmailNotFoundException
     * @return une réponse HTTP 401 avec un code d'erreur spécifique
     */
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleEmailNotFoundException(EmailNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("EMAIL_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Gère l'exception levée lorsqu'un mot de passe incorrect est fourni lors de
     * l'authentification.
     *
     * @param ex l'exception WrongPasswordException
     * @return une réponse HTTP 401 avec un code d'erreur spécifique
     */
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorEntity> handleWrongPasswordException(WrongPasswordException ex) {
        ErrorEntity error = new ErrorEntity("WRONG_PASSWORD", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Gère l'exception levée lorsqu'une erreur inattendue survient lors de la
     * tentative de connexion.
     *
     * @param ex l'exception UnexpectedLoginException
     * @return une réponse HTTP 500 avec un code d'erreur spécifique
     */
    @ExceptionHandler(UnexpectedLoginException.class)
    public ResponseEntity<ErrorEntity> handleUnexpectedLoginException(UnexpectedLoginException ex) {
        ErrorEntity error = new ErrorEntity("UNEXPECTED_LOGIN_ERROR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Gère l'exception ConversationForbiddenException.
     * @param ex l'exception levée
     * @return une réponse HTTP 403 avec l'entité d'erreur
     */
    @ExceptionHandler(ConversationForbiddenException.class)
    public ResponseEntity<ErrorEntity> handleConversationForbidden(ConversationForbiddenException ex) {
        ErrorEntity error = new ErrorEntity("FORBIDDEN", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Gère l'exception ConversationAlreadyExistsException.
     * @param ex l'exception levée
     * @return une réponse HTTP 409 avec l'entité d'erreur
     */
    @ExceptionHandler(ConversationAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleConversationAlreadyExists(ConversationAlreadyExistsException ex) {
        ErrorEntity error = new ErrorEntity("CONVERSATION_ALREADY_EXISTS", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Gère l'exception UserNotFoundException.
     * @param ex l'exception levée
     * @return une réponse HTTP 404 avec l'entité d'erreur
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleUserNotFound(UserNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("USER_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Gère l'exception WrongPasswordException.
     * @param ex l'exception levée
     * @return une réponse HTTP 401 avec l'entité d'erreur
     */
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorEntity> handleWrongPassword(WrongPasswordException ex) {
        ErrorEntity error = new ErrorEntity("WRONG_PASSWORD", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Gère l'exception EmailNotFoundException.
     * @param ex l'exception levée
     * @return une réponse HTTP 404 avec l'entité d'erreur
     */
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleEmailNotFound(EmailNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("EMAIL_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Gère l'exception UnexpectedLoginException.
     * @param ex l'exception levée
     * @return une réponse HTTP 400 avec l'entité d'erreur
     */
    @ExceptionHandler(UnexpectedLoginException.class)
    public ResponseEntity<ErrorEntity> handleUnexpectedLogin(UnexpectedLoginException ex) {
        ErrorEntity error = new ErrorEntity("UNEXPECTED_LOGIN", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
