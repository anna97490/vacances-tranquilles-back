package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    @DisplayName("Constructor with message and cause should create exception")
    void constructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Utilisateur non trouvé";
        Throwable cause = new RuntimeException("Cause originale");

        UserNotFoundException exception = new UserNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Exception should extend RuntimeException")
    void exception_ShouldExtendRuntimeException() {
        String message = "Utilisateur non trouvé";

        UserNotFoundException exception = new UserNotFoundException(message);

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Constructor with null message should create exception")
    void constructor_WithNullMessage_ShouldCreateException() {
        UserNotFoundException exception = new UserNotFoundException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Constructor with null cause should create exception")
    void constructor_WithNullCause_ShouldCreateException() {
        String message = "Utilisateur non trouvé";

        UserNotFoundException exception = new UserNotFoundException(message, null);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    // Tests pour les constructeurs et messages d'erreur

    @Test
    @DisplayName("Constructor with empty message should create exception")
    void constructor_WithEmptyMessage_ShouldCreateException() {
        UserNotFoundException exception = new UserNotFoundException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    @DisplayName("Constructor with long message should create exception")
    void constructor_WithLongMessage_ShouldCreateException() {
        String longMessage = "A".repeat(1000);
        UserNotFoundException exception = new UserNotFoundException(longMessage);

        assertNotNull(exception);
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Constructor with special characters in message should create exception")
    void constructor_WithSpecialCharacters_ShouldCreateException() {
        String specialMessage = "Utilisateur avec ID 123-456-789 non trouvé (émojis: 😀🎉)";
        UserNotFoundException exception = new UserNotFoundException(specialMessage);

        assertNotNull(exception);
        assertEquals(specialMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Constructor with only message should create exception")
    void constructor_WithOnlyMessage_ShouldCreateException() {
        String message = "Utilisateur avec ID 123 non trouvé";
        UserNotFoundException exception = new UserNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Constructor with message and exception cause should create exception")
    void constructor_WithMessageAndExceptionCause_ShouldCreateException() {
        String message = "Utilisateur non trouvé";
        Exception cause = new IllegalArgumentException("ID invalide");

        UserNotFoundException exception = new UserNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    @DisplayName("Constructor with message and error cause should create exception")
    void constructor_WithMessageAndErrorCause_ShouldCreateException() {
        String message = "Utilisateur non trouvé";
        Error cause = new OutOfMemoryError("Mémoire insuffisante");

        UserNotFoundException exception = new UserNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getCause() instanceof OutOfMemoryError);
    }

    // Tests pour les cas d'utilisation spécifiques

    @ParameterizedTest
    @CsvSource({
        "999, Utilisateur avec ID 999 non trouvé, 999",
        "nonexistent@example.com, Aucun utilisateur trouvé avec l'email : nonexistent@example.com, nonexistent@example.com",
        "utilisateur_inexistant, Utilisateur avec nom d'utilisateur 'utilisateur_inexistant' non trouvé, utilisateur_inexistant"
    })
    @DisplayName("Exception should work with business logic - user not found by various criteria")
    void exception_ShouldWorkWithBusinessLogic_UserNotFoundByVariousCriteria(String identifier, String expectedMessage, String expectedContained) {
        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains(expectedContained));
    }

    @Test
    @DisplayName("Exception should work with business logic - deleted user")
    void exception_ShouldWorkWithBusinessLogic_DeletedUser() {
        Long userId = 123L;
        String expectedMessage = "Utilisateur avec ID " + userId + " a été supprimé";

        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("supprimé"));
    }

    @Test
    @DisplayName("Exception should work with business logic - anonymized user")
    void exception_ShouldWorkWithBusinessLogic_AnonymizedUser() {
        Long userId = 456L;
        String expectedMessage = "Utilisateur avec ID " + userId + " a été anonymisé";

        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("anonymisé"));
    }

    // Tests pour les cas limites

    @Test
    @DisplayName("Exception should handle null message and null cause")
    void exception_ShouldHandleNullMessageAndNullCause() {
        UserNotFoundException exception = new UserNotFoundException(null, null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Exception should handle empty message and null cause")
    void exception_ShouldHandleEmptyMessageAndNullCause() {
        UserNotFoundException exception = new UserNotFoundException("", null);

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Exception should handle null message and exception cause")
    void exception_ShouldHandleNullMessageAndExceptionCause() {
        Exception cause = new RuntimeException("Cause");
        UserNotFoundException exception = new UserNotFoundException(null, cause);

        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    // Tests pour les cas d'erreur de conversion

    @Test
    @DisplayName("Exception should work with different exception types as cause")
    void exception_ShouldWorkWithDifferentExceptionTypesAsCause() {
        String message = "Utilisateur non trouvé";
        
        // Test avec NullPointerException
        NullPointerException npeCause = new NullPointerException("Null pointer");
        UserNotFoundException exception1 = new UserNotFoundException(message, npeCause);
        assertEquals(npeCause, exception1.getCause());
        
        // Test avec IllegalArgumentException
        IllegalArgumentException iaeCause = new IllegalArgumentException("Argument invalide");
        UserNotFoundException exception2 = new UserNotFoundException(message, iaeCause);
        assertEquals(iaeCause, exception2.getCause());
        
        // Test avec RuntimeException
        RuntimeException rteCause = new RuntimeException("Runtime error");
        UserNotFoundException exception3 = new UserNotFoundException(message, rteCause);
        assertEquals(rteCause, exception3.getCause());
    }

    @Test
    @DisplayName("Exception should work with chained exceptions")
    void exception_ShouldWorkWithChainedExceptions() {
        String message = "Utilisateur non trouvé";
        RuntimeException originalCause = new RuntimeException("Cause originale");
        IllegalArgumentException chainedCause = new IllegalArgumentException("Argument invalide", originalCause);
        UserNotFoundException exception = new UserNotFoundException(message, chainedCause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(chainedCause, exception.getCause());
        assertEquals(originalCause, exception.getCause().getCause());
    }

    // Tests pour les cas d'erreur de validation

    @Test
    @DisplayName("Exception should work with validation error messages")
    void exception_ShouldWorkWithValidationErrorMessages() {
        String validationMessage = "L'utilisateur avec l'email 'test@example.com' n'existe pas dans la base de données";
        UserNotFoundException exception = new UserNotFoundException(validationMessage);

        assertNotNull(exception);
        assertEquals(validationMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("test@example.com"));
        assertTrue(exception.getMessage().contains("n'existe pas"));
    }

    @Test
    @DisplayName("Exception should work with database error messages")
    void exception_ShouldWorkWithDatabaseErrorMessages() {
        String dbMessage = "Aucun enregistrement trouvé dans la table 'users' pour l'ID: 789";
        UserNotFoundException exception = new UserNotFoundException(dbMessage);

        assertNotNull(exception);
        assertEquals(dbMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("table 'users'"));
        assertTrue(exception.getMessage().contains("789"));
    }

    // Tests pour les cas limites de performance

    @Test
    @DisplayName("Exception should handle very long messages")
    void exception_ShouldHandleVeryLongMessages() {
        String longMessage = "A".repeat(10000);
        UserNotFoundException exception = new UserNotFoundException(longMessage);

        assertNotNull(exception);
        assertEquals(longMessage, exception.getMessage());
        assertEquals(10000, exception.getMessage().length());
    }

    @Test
    @DisplayName("Exception should handle unicode characters in messages")
    void exception_ShouldHandleUnicodeCharactersInMessages() {
        String unicodeMessage = "Utilisateur avec nom 'José María O'Connor' non trouvé (émojis: 😀🎉)";
        UserNotFoundException exception = new UserNotFoundException(unicodeMessage);

        assertNotNull(exception);
        assertEquals(unicodeMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("José María"));
        assertTrue(exception.getMessage().contains("O'Connor"));
        assertTrue(exception.getMessage().contains("😀"));
        assertTrue(exception.getMessage().contains("🎉"));
    }

    // Tests pour les cas d'erreur de business logic

    @Test
    @DisplayName("Exception should work with business logic - user search by multiple criteria")
    void exception_ShouldWorkWithBusinessLogic_UserSearchByMultipleCriteria() {
        String searchCriteria = "email: test@example.com, username: testuser, role: CLIENT";
        String expectedMessage = "Aucun utilisateur trouvé avec les critères : " + searchCriteria;

        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("test@example.com"));
        assertTrue(exception.getMessage().contains("testuser"));
        assertTrue(exception.getMessage().contains("CLIENT"));
    }

    @Test
    @DisplayName("Exception should work with business logic - user not found in specific context")
    void exception_ShouldWorkWithBusinessLogic_UserNotFoundInSpecificContext() {
        String context = "réservation de service";
        Long userId = 123L;
        String expectedMessage = "Utilisateur avec ID " + userId + " non trouvé dans le contexte : " + context;

        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("123"));
        assertTrue(exception.getMessage().contains("réservation de service"));
    }

    // Tests pour les cas d'erreur de sécurité

    @Test
    @DisplayName("Exception should work with security context - user not found in session")
    void exception_ShouldWorkWithSecurityContext_UserNotFoundInSession() {
        String sessionId = "session_12345";
        String expectedMessage = "Utilisateur non trouvé dans la session : " + sessionId;

        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("session_12345"));
    }

    @Test
    @DisplayName("Exception should work with security context - user not found in token")
    void exception_ShouldWorkWithSecurityContext_UserNotFoundInToken() {
        String tokenId = "jwt_token_67890";
        String expectedMessage = "Utilisateur associé au token '" + tokenId + "' non trouvé";

        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getMessage().contains("jwt_token_67890"));
    }

} 