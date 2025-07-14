package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnexpectedLoginExceptionTest {

    @Test
    void constructor_setsMessageAndCause() {
        String message = "Erreur inattendue lors du login";
        Throwable cause = new RuntimeException("cause");
        UnexpectedLoginException ex = new UnexpectedLoginException(message, cause);

        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
} 