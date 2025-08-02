package com.mastere_project.vacances_tranquilles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Contrôleur global de gestion des exceptions pour l'application.
 * Intercepte les exceptions personnalisées et de validation afin de fournir des
 * réponses HTTP appropriées.
 */
@ControllerAdvice
public class ApplicationControllerAdvice {

    /**
     * Gère l'exception levée lorsqu'un email existe déjà en base.
     * 
     * @param ex l'exception EmailAlreadyExistsException
     * @return une réponse HTTP 409 avec un code d'erreur spécifique
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorEntity error = new ErrorEntity("EMAIL_ALREADY_USED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409
    }

    /**
     * Gère l'exception levée lorsqu'un champ obligatoire est manquant.
     * 
     * @param ex l'exception MissingFieldException
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
     */
    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<ErrorEntity> handleMissingField(MissingFieldException ex) {
        ErrorEntity error = new ErrorEntity("MISSING_REQUIRED_FIELD", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs de validation des arguments de méthode (ex : @Valid).
     * 
     * @param ex l'exception MethodArgumentNotValidException
     * @return une réponse HTTP 400 avec un message détaillé des erreurs de
     *         validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorEntity> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s", fieldError.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        ErrorEntity error = new ErrorEntity("VALIDATION_ERROR", message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions IllegalArgumentException et retourne une réponse HTTP 400
     * (BAD_REQUEST).
     *
     * @param ex l'exception IllegalArgumentException interceptée
     * @return une réponse HTTP 400 avec le code et
     *         le message d'erreur
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorEntity> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorEntity error = new ErrorEntity("INVALID_ARGUMENT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère l'exception levée lorsqu'un compte utilisateur est temporairement bloqué
     * suite à trop de tentatives échouées.
     *
     * @param ex l'exception AccountLockedException
     * @return une réponse HTTP 423 avec un code d'erreur spécifique
     */
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
     * Gère l'exception levée lorsqu'un service n'est pas trouvé en base.
     *
     * @param ex l'exception ServiceNotFoundException
     * @return une réponse HTTP 404 avec un code d'erreur spécifique
     */
    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleServiceNotFound(ServiceNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("SERVICE_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère l'exception levée lorsqu'une conversation n'est pas trouvée.
     *
     * @param ex l'exception ConversationNotFoundException
     * @return une réponse HTTP 404 avec un code d'erreur spécifique
     */
    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleConversationNotFound(ConversationNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("CONVERSATION_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère l'exception levée lorsqu'un accès est interdit à une conversation.
     *
     * @param ex l'exception ConversationForbiddenException
     * @return une réponse HTTP 403 avec un code d'erreur spécifique
     */
    @ExceptionHandler(ConversationForbiddenException.class)
    public ResponseEntity<ErrorEntity> handleConversationForbidden(ConversationForbiddenException ex) {
        ErrorEntity error = new ErrorEntity("FORBIDDEN", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Gère l'exception levée lorsqu'une conversation existe déjà.
     *
     * @param ex l'exception ConversationAlreadyExistsException
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
     */
    @ExceptionHandler(ConversationAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleConversationAlreadyExists(ConversationAlreadyExistsException ex) {
        ErrorEntity error = new ErrorEntity("CONVERSATION_ALREADY_EXISTS", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère l'exception levée lorsqu'un utilisateur n'est pas trouvé.
     *
     * @param ex l'exception UserNotFoundException
     * @return une réponse HTTP 404 avec un code d'erreur spécifique
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleUserNotFound(UserNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("USER_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère toute autre exception non spécifiquement capturée.
     *
     * @param ex l'exception levée
     * @return une réponse HTTP 500 avec un message d'erreur générique
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorEntity> handleAllExceptions(Exception ex) {
        ErrorEntity error = new ErrorEntity("INTERNAL_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
