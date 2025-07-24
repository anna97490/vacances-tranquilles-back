package com.mastere_project.vacances_tranquilles.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ErrorEntityTest {
    @Test
    void constructor_and_getters_setters_shouldWork() {
        ErrorEntity error = new ErrorEntity("CODE", "Message d'erreur");
        assertThat(error.getCode()).isEqualTo("CODE");
        assertThat(error.getMessage()).isEqualTo("Message d'erreur");

        error.setCode("NEW_CODE");
        error.setMessage("Nouveau message");
        assertThat(error.getCode()).isEqualTo("NEW_CODE");
        assertThat(error.getMessage()).isEqualTo("Nouveau message");
    }

    @Test
    void equals_and_hashCode_shouldWork() {
        ErrorEntity e1 = new ErrorEntity("A", "B");
        ErrorEntity e2 = new ErrorEntity("A", "B");
        ErrorEntity e3 = new ErrorEntity("X", "Y");

        assertThat(e1).isEqualTo(e2).hasSameHashCodeAs(e2).isNotEqualTo(e3);
    }

    @Test
    void toString_shouldContainFields() {
        ErrorEntity error = new ErrorEntity("C", "M");
        String str = error.toString();
        assertThat(str).contains("C").contains("M");
    }

    @Test
    void canEqual_shouldReturnTrueForSameType() {
        ErrorEntity error = new ErrorEntity("C", "M");
        assertThat(error.canEqual(new ErrorEntity("X", "Y"))).isTrue();
        assertThat(error.canEqual("not an ErrorEntity")).isFalse();
    }
} 