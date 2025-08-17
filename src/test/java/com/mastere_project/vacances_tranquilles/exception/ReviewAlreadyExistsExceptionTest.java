package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Constructor should set message correctly")
    void constructor_shouldSetMessage() {
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException("Avis déjà existant");
        assertThat(ex.getMessage()).isEqualTo("Avis déjà existant");
    }

    @Test
    @DisplayName("Constructor should handle empty message")
    void constructor_shouldHandleEmptyMessage() {
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException("");
        assertThat(ex.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Constructor should handle null message")
    void constructor_shouldHandleNullMessage() {
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException(null);
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    @DisplayName("Constructor should handle long message")
    void constructor_shouldHandleLongMessage() {
        String longMessage = "Avis déjà existant avec un message très long pour tester la gestion des messages d'erreur détaillés dans l'application de gestion des avis utilisateurs";
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException(longMessage);
        assertThat(ex.getMessage()).isEqualTo(longMessage);
    }

    // Tests pour le constructeur avec cause

    @Test
    @DisplayName("Constructor with cause should set message and cause correctly")
    void constructorWithCause_shouldSetMessageAndCause() {
        RuntimeException cause = new RuntimeException("Cause exception");
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException("Avis déjà existant", cause);
        assertThat(ex.getMessage()).isEqualTo("Avis déjà existant");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Constructor with cause should handle null cause")
    void constructorWithCause_shouldHandleNullCause() {
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException("Avis déjà existant", null);
        assertThat(ex.getMessage()).isEqualTo("Avis déjà existant");
        assertThat(ex.getCause()).isNull();
    }

    // Tests pour les messages d'erreur personnalisés

    @Test
    @DisplayName("Exception should have custom error message for review already exists")
    void exception_shouldHaveCustomErrorMessageForReviewAlreadyExists() {
        String customMessage = "Vous avez déjà créé un avis pour cette réservation. Un seul avis par réservation est autorisé.";
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException(customMessage);
        assertThat(ex.getMessage()).isEqualTo(customMessage);
        assertThat(ex.getMessage()).contains("déjà créé un avis");
        assertThat(ex.getMessage()).contains("Un seul avis par réservation");
    }

    @Test
    @DisplayName("Exception should handle special characters in message")
    void exception_shouldHandleSpecialCharactersInMessage() {
        String specialMessage = "Avis déjà existant: ReservationID=123, UserID=456, Date=2024-01-15, Status=EXISTS";
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException(specialMessage);
        assertThat(ex.getMessage()).isEqualTo(specialMessage);
        assertThat(ex.getMessage()).contains("=");
        assertThat(ex.getMessage()).contains("ID");
    }

    // Tests pour les cas d'utilisation des exceptions

    @Test
    @DisplayName("Exception should be throwable")
    void exception_shouldBeThrowable() {
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException("Test exception");
        
        assertThat(ex).isInstanceOf(RuntimeException.class)
                     .isInstanceOf(ReviewAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Exception should maintain message when thrown and caught")
    void exception_shouldMaintainMessageWhenThrownAndCaught() {
        String errorMessage = "Avis déjà existant pour cette réservation";
        ReviewAlreadyExistsException thrownException = null;
        
        try {
            throw new ReviewAlreadyExistsException(errorMessage);
        } catch (ReviewAlreadyExistsException e) {
            thrownException = e;
        }
        
        assertThat(thrownException).isNotNull();
        assertThat(thrownException.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Exception should work with different message formats")
    void exception_shouldWorkWithDifferentMessageFormats() {
        // Test avec message simple
        ReviewAlreadyExistsException ex1 = new ReviewAlreadyExistsException("Simple message");
        assertThat(ex1.getMessage()).isEqualTo("Simple message");
        
        // Test avec message contenant des IDs
        ReviewAlreadyExistsException ex2 = new ReviewAlreadyExistsException("Review already exists for reservation 123");
        assertThat(ex2.getMessage()).isEqualTo("Review already exists for reservation 123");
        
        // Test avec message contenant des caractères spéciaux
        ReviewAlreadyExistsException ex3 = new ReviewAlreadyExistsException("Avis #123-456 déjà existant");
        assertThat(ex3.getMessage()).isEqualTo("Avis #123-456 déjà existant");
    }

    @Test
    @DisplayName("Exception should handle edge case messages")
    void exception_shouldHandleEdgeCaseMessages() {
        // Test avec message très court
        ReviewAlreadyExistsException ex1 = new ReviewAlreadyExistsException("!");
        assertThat(ex1.getMessage()).isEqualTo("!");
        
        // Test avec message contenant des espaces multiples
        ReviewAlreadyExistsException ex2 = new ReviewAlreadyExistsException("   Avis   déjà   existant   ");
        assertThat(ex2.getMessage()).isEqualTo("   Avis   déjà   existant   ");
        
        // Test avec message contenant des caractères unicode
        ReviewAlreadyExistsException ex3 = new ReviewAlreadyExistsException("Avis déjà existant: éàçù");
        assertThat(ex3.getMessage()).isEqualTo("Avis déjà existant: éàçù");
    }

    @Test
    @DisplayName("Exception should handle business logic error messages")
    void exception_shouldHandleBusinessLogicErrorMessages() {
        // Test avec message d'erreur métier
        String businessMessage = "Vous avez déjà créé un avis pour cette réservation. Un seul avis par réservation est autorisé.";
        ReviewAlreadyExistsException ex = new ReviewAlreadyExistsException(businessMessage);
        assertThat(ex.getMessage()).isEqualTo(businessMessage);
        assertThat(ex.getMessage()).contains("déjà créé un avis");
        assertThat(ex.getMessage()).contains("Un seul avis par réservation");
        
        // Test avec message d'erreur de validation
        String validationMessage = "Un avis existe déjà pour cette réservation et cet utilisateur";
        ReviewAlreadyExistsException ex2 = new ReviewAlreadyExistsException(validationMessage);
        assertThat(ex2.getMessage()).isEqualTo(validationMessage);
        assertThat(ex2.getMessage()).contains("existe déjà");
    }

    @Test
    @DisplayName("Exception should handle multiple error scenarios")
    void exception_shouldHandleMultipleErrorScenarios() {
        // Test avis déjà créé
        ReviewAlreadyExistsException ex1 = new ReviewAlreadyExistsException("Vous avez déjà créé un avis pour cette réservation");
        assertThat(ex1.getMessage()).contains("déjà créé un avis");
        
        // Test limite d'avis
        ReviewAlreadyExistsException ex2 = new ReviewAlreadyExistsException("Un seul avis par réservation est autorisé");
        assertThat(ex2.getMessage()).contains("Un seul avis par réservation");
        
        // Test duplication
        ReviewAlreadyExistsException ex3 = new ReviewAlreadyExistsException("Avis en double détecté");
        assertThat(ex3.getMessage()).contains("en double");
    }
}
