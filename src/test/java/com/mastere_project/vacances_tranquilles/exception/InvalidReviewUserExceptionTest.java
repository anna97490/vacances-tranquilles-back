package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

class InvalidReviewUserExceptionTest {
    

    @Test
    @DisplayName("Constructor should set message correctly")
    void constructor_shouldSetMessage() {
        InvalidReviewUserException ex = new InvalidReviewUserException("Erreur utilisateur review");
        assertThat(ex.getMessage()).isEqualTo("Erreur utilisateur review");
    }

    @Test
    @DisplayName("Constructor should handle empty message")
    void constructor_shouldHandleEmptyMessage() {
        InvalidReviewUserException ex = new InvalidReviewUserException("");
        assertThat(ex.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Constructor should handle null message")
    void constructor_shouldHandleNullMessage() {
        InvalidReviewUserException ex = new InvalidReviewUserException(null);
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    @DisplayName("Constructor should handle long message")
    void constructor_shouldHandleLongMessage() {
        String longMessage = "Exception utilisateur invalide avec un message très long pour tester la gestion des messages d'erreur détaillés dans l'application de gestion des avis utilisateurs";
        InvalidReviewUserException ex = new InvalidReviewUserException(longMessage);
        assertThat(ex.getMessage()).isEqualTo(longMessage);
    }

    // Tests pour les messages d'erreur personnalisés

    @Test
    @DisplayName("Exception should have custom error message for invalid review user")
    void exception_shouldHaveCustomErrorMessageForInvalidReviewUser() {
        String customMessage = "L'utilisateur ID 123 ne peut pas évaluer l'utilisateur ID 456 car ils ne correspondent pas à la réservation";
        InvalidReviewUserException ex = new InvalidReviewUserException(customMessage);
        assertThat(ex.getMessage()).isEqualTo(customMessage);
        assertThat(ex.getMessage()).contains("123");
        assertThat(ex.getMessage()).contains("456");
        assertThat(ex.getMessage()).contains("ne peut pas évaluer");
    }

    @Test
    @DisplayName("Exception should handle special characters in message")
    void exception_shouldHandleSpecialCharactersInMessage() {
        String specialMessage = "Utilisateur invalide: UserID=123, TargetID=456, ReservationID=789, Status=INVALID";
        InvalidReviewUserException ex = new InvalidReviewUserException(specialMessage);
        assertThat(ex.getMessage()).isEqualTo(specialMessage);
        assertThat(ex.getMessage()).contains("=");
        assertThat(ex.getMessage()).contains("ID");
    }

    // Tests pour les cas d'utilisation des exceptions

    @Test
    @DisplayName("Exception should be throwable")
    void exception_shouldBeThrowable() {
        InvalidReviewUserException ex = new InvalidReviewUserException("Test exception");
        
        assertThat(ex).isInstanceOf(RuntimeException.class)
                     .isInstanceOf(InvalidReviewUserException.class);
    }

    @Test
    @DisplayName("Exception should maintain message when thrown and caught")
    void exception_shouldMaintainMessageWhenThrownAndCaught() {
        String errorMessage = "Utilisateur invalide pour cette action";
        InvalidReviewUserException thrownException = null;
        
        try {
            throw new InvalidReviewUserException(errorMessage);
        } catch (InvalidReviewUserException e) {
            thrownException = e;
        }
        
        assertThat(thrownException).isNotNull();
        assertThat(thrownException.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Exception should work with different message formats")
    void exception_shouldWorkWithDifferentMessageFormats() {
        // Test avec message simple
        InvalidReviewUserException ex1 = new InvalidReviewUserException("Simple message");
        assertThat(ex1.getMessage()).isEqualTo("Simple message");
        
        // Test avec message contenant des IDs
        InvalidReviewUserException ex2 = new InvalidReviewUserException("User 123 cannot review user 456");
        assertThat(ex2.getMessage()).isEqualTo("User 123 cannot review user 456");
        
        // Test avec message contenant des caractères spéciaux
        InvalidReviewUserException ex3 = new InvalidReviewUserException("Utilisateur #123-456 invalide");
        assertThat(ex3.getMessage()).isEqualTo("Utilisateur #123-456 invalide");
    }

    @Test
    @DisplayName("Exception should handle edge case messages")
    void exception_shouldHandleEdgeCaseMessages() {
        // Test avec message très court
        InvalidReviewUserException ex1 = new InvalidReviewUserException("!");
        assertThat(ex1.getMessage()).isEqualTo("!");
        
        // Test avec message contenant des espaces multiples
        InvalidReviewUserException ex2 = new InvalidReviewUserException("   Utilisateur   invalide   ");
        assertThat(ex2.getMessage()).isEqualTo("   Utilisateur   invalide   ");
        
        // Test avec message contenant des caractères unicode
        InvalidReviewUserException ex3 = new InvalidReviewUserException("Utilisateur invalide: éàçù");
        assertThat(ex3.getMessage()).isEqualTo("Utilisateur invalide: éàçù");
    }

    @Test
    @DisplayName("Exception should handle business logic error messages")
    void exception_shouldHandleBusinessLogicErrorMessages() {
        // Test avec message d'erreur métier
        String businessMessage = "Vous ne pouvez créer un avis qu'en votre nom";
        InvalidReviewUserException ex = new InvalidReviewUserException(businessMessage);
        assertThat(ex.getMessage()).isEqualTo(businessMessage);
        assertThat(ex.getMessage()).contains("ne pouvez créer un avis");
        
        // Test avec message d'erreur de validation
        String validationMessage = "Les utilisateurs ne correspondent pas à la réservation";
        InvalidReviewUserException ex2 = new InvalidReviewUserException(validationMessage);
        assertThat(ex2.getMessage()).isEqualTo(validationMessage);
        assertThat(ex2.getMessage()).contains("ne correspondent pas");
    }

    @Test
    @DisplayName("Exception should handle multiple error scenarios")
    void exception_shouldHandleMultipleErrorScenarios() {
        // Test auto-évaluation
        InvalidReviewUserException ex1 = new InvalidReviewUserException("Un utilisateur ne peut pas s'auto-évaluer");
        assertThat(ex1.getMessage()).contains("auto-évaluer");
        
        // Test utilisateur non autorisé
        InvalidReviewUserException ex2 = new InvalidReviewUserException("Utilisateur non autorisé pour cette action");
        assertThat(ex2.getMessage()).contains("non autorisé");
        
        // Test violation des règles de notation
        InvalidReviewUserException ex3 = new InvalidReviewUserException("Violation des règles de notation");
        assertThat(ex3.getMessage()).contains("règles de notation");
    }
} 