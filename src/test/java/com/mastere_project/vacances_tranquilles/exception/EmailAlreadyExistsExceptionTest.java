package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailAlreadyExistsExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email déjà utilisé");
        
        assertThat(ex.getMessage()).isEqualTo("Email déjà utilisé");
    }
} 