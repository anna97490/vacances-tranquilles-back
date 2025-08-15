package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewNotFoundExceptionTest {

    @Test
    @DisplayName("Constructor should set message correctly")
    void constructor_shouldSetMessage() {
        ReviewNotFoundException ex = new ReviewNotFoundException("Avis introuvable");
        assertThat(ex.getMessage()).isEqualTo("Avis introuvable");
    }

    @Test
    @DisplayName("Constructor should handle empty message")
    void constructor_shouldHandleEmptyMessage() {
        ReviewNotFoundException ex = new ReviewNotFoundException("");
        assertThat(ex.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Constructor should handle null message")
    void constructor_shouldHandleNullMessage() {
        ReviewNotFoundException ex = new ReviewNotFoundException(null);
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    @DisplayName("Constructor should handle long message")
    void constructor_shouldHandleLongMessage() {
        String longMessage = "Avis introuvable avec un message très long pour tester la gestion des messages d'erreur détaillés dans l'application de gestion des vacances tranquilles";
        ReviewNotFoundException ex = new ReviewNotFoundException(longMessage);
        assertThat(ex.getMessage()).isEqualTo(longMessage);
    }

    // Tests pour les messages d'erreur personnalisés

    @Test
    @DisplayName("Exception should have custom error message for review not found")
    void exception_shouldHaveCustomErrorMessageForReviewNotFound() {
        String customMessage = "L'avis avec l'ID 123 n'a pas été trouvé dans la base de données";
        ReviewNotFoundException ex = new ReviewNotFoundException(customMessage);
        assertThat(ex.getMessage()).isEqualTo(customMessage);
        assertThat(ex.getMessage()).contains("123");
        assertThat(ex.getMessage()).contains("n'a pas été trouvé");
    }

    @Test
    @DisplayName("Exception should handle special characters in message")
    void exception_shouldHandleSpecialCharactersInMessage() {
        String specialMessage = "Avis introuvable: ID=123, User=456, Date=2024-01-15, Status=DELETED";
        ReviewNotFoundException ex = new ReviewNotFoundException(specialMessage);
        assertThat(ex.getMessage()).isEqualTo(specialMessage);
        assertThat(ex.getMessage()).contains("=");
        assertThat(ex.getMessage()).contains("-");
    }

    // Tests pour les cas d'utilisation des exceptions

    @Test
    @DisplayName("Exception should be throwable")
    void exception_shouldBeThrowable() {
        ReviewNotFoundException ex = new ReviewNotFoundException("Test exception");
        
        assertThat(ex).isInstanceOf(RuntimeException.class);
        assertThat(ex).isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    @DisplayName("Exception should maintain message when thrown and caught")
    void exception_shouldMaintainMessageWhenThrownAndCaught() {
        String errorMessage = "Avis introuvable pour l'utilisateur";
        ReviewNotFoundException thrownException = null;
        
        try {
            throw new ReviewNotFoundException(errorMessage);
        } catch (ReviewNotFoundException e) {
            thrownException = e;
        }
        
        assertThat(thrownException).isNotNull();
        assertThat(thrownException.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Exception should work with different message formats")
    void exception_shouldWorkWithDifferentMessageFormats() {
        // Test avec message simple
        ReviewNotFoundException ex1 = new ReviewNotFoundException("Simple message");
        assertThat(ex1.getMessage()).isEqualTo("Simple message");
        
        // Test avec message contenant des chiffres
        ReviewNotFoundException ex2 = new ReviewNotFoundException("Review ID 456 not found");
        assertThat(ex2.getMessage()).isEqualTo("Review ID 456 not found");
        
        // Test avec message contenant des caractères spéciaux
        ReviewNotFoundException ex3 = new ReviewNotFoundException("Avis #123-456 introuvable");
        assertThat(ex3.getMessage()).isEqualTo("Avis #123-456 introuvable");
    }

    @Test
    @DisplayName("Exception should handle edge case messages")
    void exception_shouldHandleEdgeCaseMessages() {
        // Test avec message très court
        ReviewNotFoundException ex1 = new ReviewNotFoundException("!");
        assertThat(ex1.getMessage()).isEqualTo("!");
        
        // Test avec message contenant des espaces multiples
        ReviewNotFoundException ex2 = new ReviewNotFoundException("   Avis   introuvable   ");
        assertThat(ex2.getMessage()).isEqualTo("   Avis   introuvable   ");
        
        // Test avec message contenant des caractères unicode
        ReviewNotFoundException ex3 = new ReviewNotFoundException("Avis introuvable: éàçù");
        assertThat(ex3.getMessage()).isEqualTo("Avis introuvable: éàçù");
    }
}
