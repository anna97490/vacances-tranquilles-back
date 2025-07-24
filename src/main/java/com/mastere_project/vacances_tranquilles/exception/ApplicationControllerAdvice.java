package com.mastere_project.vacances_tranquilles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Contrôleur global de gestion des exceptions.
 * Intercepte les exceptions personnalisées et retourne une réponse structurée.
 */
@ControllerAdvice
public class ApplicationControllerAdvice {

    /**
     * Gère les cas où un email existe déjà.
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
     * Gère les cas de champ manquant.
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
     * Gère les erreurs de validation des champs.
     *
     * @param ex l'exception MethodArgumentNotValidException
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
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
     * Gère les cas où un argument illégal est fourni.
     *
     * @param ex l'exception IllegalArgumentException
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorEntity> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorEntity error = new ErrorEntity("INVALID_ARGUMENT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les exceptions non spécifiquement gérées.
     *
     * @param ex l'exception générique
     * @return une réponse HTTP 500 avec un code d'erreur générique
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorEntity> handleAllExceptions(Exception ex) {
        ErrorEntity error = new ErrorEntity("INTERNAL_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère les cas où une réservation est introuvable.
     *
     * @param ex l'exception ReservationNotFoundException
     * @return une réponse HTTP 404 avec un code d'erreur spécifique
     */
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleReservationNotFound(ReservationNotFoundException ex) {
        ErrorEntity error = new ErrorEntity("RESERVATION_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les cas d'accès non autorisé à une réservation.
     *
     * @param ex l'exception UnauthorizedReservationAccessException
     * @return une réponse HTTP 403 avec un code d'erreur spécifique
     */
    @ExceptionHandler(UnauthorizedReservationAccessException.class)
    public ResponseEntity<ErrorEntity> handleUnauthorizedAccess(UnauthorizedReservationAccessException ex) {
        ErrorEntity error = new ErrorEntity("UNAUTHORIZED_ACCESS", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Gère les cas où les données nécessaires à une réservation sont absentes.
     *
     * @param ex l'exception MissingReservationDataException
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
     */
    @ExceptionHandler(MissingReservationDataException.class)
    public ResponseEntity<ErrorEntity> handleMissingReservationData(MissingReservationDataException ex) {
        ErrorEntity error = new ErrorEntity("MISSING_RESERVATION_DATA", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les cas où un statut de réservation invalide est fourni.
     *
     * @param ex l'exception InvalidReservationStatusException
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
     */
    @ExceptionHandler(InvalidReservationStatusException.class)
    public ResponseEntity<ErrorEntity> handleInvalidReservationStatus(InvalidReservationStatusException ex) {
        ErrorEntity error = new ErrorEntity("INVALID_RESERVATION_STATUS", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les cas où une transition de statut de réservation invalide est tentée.
     *
     * @param ex l'exception InvalidReservationStatusTransitionException
     * @return une réponse HTTP 400 avec un code d'erreur spécifique
     */
    @ExceptionHandler(InvalidReservationStatusTransitionException.class)
    public ResponseEntity<ErrorEntity> handleInvalidReservationStatusTransition(InvalidReservationStatusTransitionException ex) {
        ErrorEntity error = new ErrorEntity("INVALID_RESERVATION_STATUS_TRANSITION", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
