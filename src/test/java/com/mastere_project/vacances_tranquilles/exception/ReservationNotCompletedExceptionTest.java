package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationNotCompletedExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        ReservationNotCompletedException ex = new ReservationNotCompletedException("La réservation n'a pas été finalisée");
        assertThat(ex.getMessage()).isEqualTo("La réservation n'a pas été finalisée");
    }
}
