package com.mastere_project.vacances_tranquilles.model.enums;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitaires pour l'enum UserRole.
 */
class UserRoleTest {

    @Test
    void shouldHaveThreeValues() {
        // Given & When
        UserRole[] values = UserRole.values();
        
        // Then
        assertThat(values).hasSize(3);
    }

    @Test
    void shouldContainExpectedValues() {
        // Given & When
        UserRole[] values = UserRole.values();
        
        // Then
        assertThat(values).contains(UserRole.CLIENT);
        assertThat(values).contains(UserRole.PROVIDER);
        assertThat(values).contains(UserRole.ADMIN);
    }

    @Test
    void shouldReturnCorrectValueOf() {
        // Given & When & Then
        assertThat(UserRole.valueOf("CLIENT")).isEqualTo(UserRole.CLIENT);
        assertThat(UserRole.valueOf("PROVIDER")).isEqualTo(UserRole.PROVIDER);
        assertThat(UserRole.valueOf("ADMIN")).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void shouldHaveCorrectOrdinalValues() {
        // Given & When & Then
        assertThat(UserRole.CLIENT.ordinal()).isEqualTo(0);
        assertThat(UserRole.PROVIDER.ordinal()).isEqualTo(1);
        assertThat(UserRole.ADMIN.ordinal()).isEqualTo(2);
    }

    @Test
    void shouldHaveCorrectNameValues() {
        // Given & When & Then
        assertThat(UserRole.CLIENT.name()).isEqualTo("CLIENT");
        assertThat(UserRole.PROVIDER.name()).isEqualTo("PROVIDER");
        assertThat(UserRole.ADMIN.name()).isEqualTo("ADMIN");
    }

    @Test
    void shouldBeComparable() {
        // Given & When & Then
        assertThat(UserRole.CLIENT.compareTo(UserRole.PROVIDER)).isLessThan(0);
        assertThat(UserRole.PROVIDER.compareTo(UserRole.ADMIN)).isLessThan(0);
        assertThat(UserRole.ADMIN.compareTo(UserRole.CLIENT)).isGreaterThan(0);
        assertThat(UserRole.CLIENT.compareTo(UserRole.CLIENT)).isEqualTo(0);
    }

    @Test
    void shouldBeEqualWhenSameInstance() {
        // Given & When & Then
        assertThat(UserRole.CLIENT).isEqualTo(UserRole.CLIENT);
        assertThat(UserRole.PROVIDER).isEqualTo(UserRole.PROVIDER);
        assertThat(UserRole.ADMIN).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void shouldNotBeEqualWhenDifferentInstances() {
        // Given & When & Then
        assertThat(UserRole.CLIENT).isNotEqualTo(UserRole.PROVIDER);
        assertThat(UserRole.CLIENT).isNotEqualTo(UserRole.ADMIN);
        assertThat(UserRole.PROVIDER).isNotEqualTo(UserRole.ADMIN);
    }

    @Test
    void shouldHaveCorrectToString() {
        // Given & When & Then
        assertThat(UserRole.CLIENT.toString()).isEqualTo("CLIENT");
        assertThat(UserRole.PROVIDER.toString()).isEqualTo("PROVIDER");
        assertThat(UserRole.ADMIN.toString()).isEqualTo("ADMIN");
    }

    @Test
    void shouldHaveCorrectHashCode() {
        // Given & When & Then
        assertThat(UserRole.CLIENT.hashCode()).isEqualTo(UserRole.CLIENT.hashCode());
        assertThat(UserRole.PROVIDER.hashCode()).isEqualTo(UserRole.PROVIDER.hashCode());
        assertThat(UserRole.ADMIN.hashCode()).isEqualTo(UserRole.ADMIN.hashCode());
    }

    @Test
    void shouldBeSerializable() {
        // Given & When & Then
        assertThat(UserRole.CLIENT).isInstanceOf(Enum.class);
        assertThat(UserRole.PROVIDER).isInstanceOf(Enum.class);
        assertThat(UserRole.ADMIN).isInstanceOf(Enum.class);
    }

    @Test
    void shouldHaveCorrectClass() {
        // Given & When & Then
        assertThat(UserRole.CLIENT.getClass()).isEqualTo(UserRole.class);
        assertThat(UserRole.PROVIDER.getClass()).isEqualTo(UserRole.class);
        assertThat(UserRole.ADMIN.getClass()).isEqualTo(UserRole.class);
    }
} 