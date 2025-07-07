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

}
