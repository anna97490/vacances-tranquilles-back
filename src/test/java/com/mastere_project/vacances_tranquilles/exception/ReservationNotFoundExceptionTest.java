package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationNotFoundExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        ReservationNotFoundException ex = new ReservationNotFoundException("Réservation introuvable");
        assertThat(ex.getMessage()).isEqualTo("Réservation introuvable");
    }
}
