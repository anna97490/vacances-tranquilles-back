package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MissingFieldExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        MissingFieldException ex = new MissingFieldException("Champ obligatoire manquant");
        assertThat(ex.getMessage()).isEqualTo("Champ obligatoire manquant");
    }
}

