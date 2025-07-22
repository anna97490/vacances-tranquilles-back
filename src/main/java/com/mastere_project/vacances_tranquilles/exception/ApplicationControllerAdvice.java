package com.mastere_project.vacances_tranquilles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Contrôleur global de gestion des exceptions pour l'application.
 * Intercepte les exceptions personnalisées et retourne une réponse structurée avec un code et un message d'erreur.
 */
@ControllerAdvice
public class ApplicationControllerAdvice {

    /**
     * Gère l'exception EmailAlreadyExistsException (email déjà utilisé).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorEntity error = new ErrorEntity("EMAIL_ALREADY_USED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409
    }

    /**
     * Gère l'exception MissingFieldException (champ requis manquant).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<ErrorEntity> handleMissingField(MissingFieldException ex) {
        ErrorEntity error = new ErrorEntity("MISSING_REQUIRED_FIELD", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs de validation des arguments de méthode (ex : @Valid).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message de validation
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
     * Gère l'exception ConversationNotFoundException (conversation non trouvée).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleConversationNotFound(ConversationNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("CONVERSATION_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère l'exception ConversationForbiddenException (accès interdit à la conversation).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(ConversationForbiddenException.class)
    public ResponseEntity<ErrorEntity> handleConversationForbidden(ConversationForbiddenException ex) {
        ErrorEntity error = new ErrorEntity("FORBIDDEN", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Gère l'exception ConversationAlreadyExistsException (conversation déjà existante).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(ConversationAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleConversationAlreadyExists(ConversationAlreadyExistsException ex) {
        ErrorEntity error = new ErrorEntity("CONVERSATION_ALREADY_EXISTS", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère l'exception UserNotFoundException (utilisateur non trouvé).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleUserNotFound(UserNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("USER_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère l'exception WrongPasswordException (mot de passe incorrect).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorEntity> handleWrongPassword(WrongPasswordException ex) {
        ErrorEntity error = new ErrorEntity("WRONG_PASSWORD", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gère l'exception EmailNotFoundException (email non trouvé).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleEmailNotFound(EmailNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("EMAIL_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère l'exception UnexpectedLoginException (erreur inattendue lors du login).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(UnexpectedLoginException.class)
    public ResponseEntity<ErrorEntity> handleUnexpectedLogin(UnexpectedLoginException ex) {
        ErrorEntity error = new ErrorEntity("UNEXPECTED_LOGIN", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère l'exception AccountLockedException (compte temporairement verrouillé).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorEntity> handleAccountLockedException(AccountLockedException ex) {
        ErrorEntity error = new ErrorEntity("ACCOUNT_LOCKED", ex.getMessage());
        return ResponseEntity.status(423).body(error);
    }

    /**
     * Gère l'exception LoginInternalException (erreur technique lors du login).
     *
     * @param ex l'exception levée
     * @return une réponse contenant le code d'erreur et le message associé
     */
    @ExceptionHandler(LoginInternalException.class)
    public ResponseEntity<ErrorEntity> handleLoginInternalException(LoginInternalException ex) {
        ErrorEntity error = new ErrorEntity("LOGIN_INTERNAL_ERROR", ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }

    /**
     * Gère l'exception IllegalArgumentException (paramètre invalide).
     *
     * @param ex l'exception levée
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorEntity> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorEntity error = new ErrorEntity("INVALID_ARGUMENT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
