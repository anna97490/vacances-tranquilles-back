package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InvalidReviewUserExceptionTest {
    
    @Test
    void constructor_shouldSetMessage() {
        InvalidReviewUserException ex = new InvalidReviewUserException("Erreur utilisateur review");
        assertThat(ex.getMessage()).isEqualTo("Erreur utilisateur review");
    }
} 