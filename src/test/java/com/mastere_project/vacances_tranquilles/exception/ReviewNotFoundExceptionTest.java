package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewNotFoundExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        ReviewNotFoundException ex = new ReviewNotFoundException("Avis introuvable");
        assertThat(ex.getMessage()).isEqualTo("Avis introuvable");
    }
}
