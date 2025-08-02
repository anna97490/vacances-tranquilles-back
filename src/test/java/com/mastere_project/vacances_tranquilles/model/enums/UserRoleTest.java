package com.mastere_project.vacances_tranquilles.model.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

class UserRoleTest {
    
    // Tests pour les valeurs de l'enum

    @Test
    @DisplayName("Enum should have CLIENT value")
    void enum_shouldHaveClientValue() {
        assertThat(UserRole.CLIENT).isNotNull()
                                 .extracting(UserRole::name)
                                 .isEqualTo("CLIENT");
    }

    @Test
    @DisplayName("Enum should have PROVIDER value")
    void enum_shouldHaveProviderValue() {
        assertThat(UserRole.PROVIDER).isNotNull()
                                    .extracting(UserRole::name)
                                    .isEqualTo("PROVIDER");
    }

    @Test
    @DisplayName("Enum should have ADMIN value")
    void enum_shouldHaveAdminValue() {
        assertThat(UserRole.ADMIN).isNotNull()
                                 .extracting(UserRole::name)
                                 .isEqualTo("ADMIN");
    }

    // Tests pour les méthodes de l'enum

    @Test
    @DisplayName("Enum values should return all roles")
    void enumValues_shouldReturnAllRoles() {
        UserRole[] values = UserRole.values();
        
        assertThat(values).hasSize(3)
                         .contains(UserRole.CLIENT)
                         .contains(UserRole.PROVIDER)
                         .contains(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Enum valueOf should work correctly")
    void enumValueOf_shouldWorkCorrectly() {
        // Test each valueOf separately as they test different enum values
        assertThat(UserRole.valueOf("CLIENT")).isEqualTo(UserRole.CLIENT);
        assertThat(UserRole.valueOf("PROVIDER")).isEqualTo(UserRole.PROVIDER);
        assertThat(UserRole.valueOf("ADMIN")).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Enum ordinal should be correct")
    void enumOrdinal_shouldBeCorrect() {
        // Test each ordinal separately as they test different enum values
        assertThat(UserRole.CLIENT.ordinal()).isZero();
        assertThat(UserRole.PROVIDER.ordinal()).isEqualTo(1);
        assertThat(UserRole.ADMIN.ordinal()).isEqualTo(2);
    }

    // Tests pour les comparaisons

    @Test
    @DisplayName("Enum comparison should work correctly")
    void enumComparison_shouldWorkCorrectly() {
        assertThat(UserRole.CLIENT).isLessThan(UserRole.PROVIDER);
        assertThat(UserRole.PROVIDER).isLessThan(UserRole.ADMIN);
        assertThat(UserRole.ADMIN).isGreaterThan(UserRole.CLIENT);
    }

    @Test
    @DisplayName("Enum should be equal to itself")
    void enum_shouldBeEqualToItself() {
        assertThat(UserRole.CLIENT).isEqualTo(UserRole.CLIENT);
        assertThat(UserRole.PROVIDER).isEqualTo(UserRole.PROVIDER);
        assertThat(UserRole.ADMIN).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("CLIENT should not be equal to PROVIDER")
    void client_shouldNotBeEqualToProvider() {
        assertThat(UserRole.CLIENT).isNotEqualTo(UserRole.PROVIDER);
    }

    @Test
    @DisplayName("CLIENT should not be equal to ADMIN")
    void client_shouldNotBeEqualToAdmin() {
        assertThat(UserRole.CLIENT).isNotEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("PROVIDER should not be equal to ADMIN")
    void provider_shouldNotBeEqualToAdmin() {
        assertThat(UserRole.PROVIDER).isNotEqualTo(UserRole.ADMIN);
    }

    // Tests pour les cas d'utilisation métier

    @Test
    @DisplayName("CLIENT role should be appropriate for customers")
    void clientRole_shouldBeAppropriateForCustomers() {
        UserRole clientRole = UserRole.CLIENT;
        
        assertThat(clientRole).isInstanceOf(UserRole.class)
                             .satisfies(role -> {
                                 assertThat(role.name()).isEqualTo("CLIENT");
                                 assertThat(role.ordinal()).isZero();
                             });
    }

    @Test
    @DisplayName("PROVIDER role should be appropriate for service providers")
    void providerRole_shouldBeAppropriateForServiceProviders() {
        UserRole providerRole = UserRole.PROVIDER;
        
        assertThat(providerRole).isInstanceOf(UserRole.class)
                               .satisfies(role -> {
                                   assertThat(role.name()).isEqualTo("PROVIDER");
                                   assertThat(role.ordinal()).isEqualTo(1);
                               });
    }

    @Test
    @DisplayName("ADMIN role should be appropriate for administrators")
    void adminRole_shouldBeAppropriateForAdministrators() {
        UserRole adminRole = UserRole.ADMIN;
        
        assertThat(adminRole).isInstanceOf(UserRole.class)
                            .satisfies(role -> {
                                assertThat(role.name()).isEqualTo("ADMIN");
                                assertThat(role.ordinal()).isEqualTo(2);
                            });
    }

    // Tests pour les conversions

    @Test
    @DisplayName("Enum should convert to string correctly")
    void enum_shouldConvertToStringCorrectly() {
        // Test each toString separately as they test different enum values
        assertThat(UserRole.CLIENT).hasToString("CLIENT");
        assertThat(UserRole.PROVIDER).hasToString("PROVIDER");
        assertThat(UserRole.ADMIN).hasToString("ADMIN");
    }

    @Test
    @DisplayName("Enum should work in switch statements")
    void enum_shouldWorkInSwitchStatements() {
        String result = "";
        
        switch (UserRole.CLIENT) {
            case CLIENT:
                result = "client";
                break;
            case PROVIDER:
                result = "provider";
                break;
            case ADMIN:
                result = "admin";
                break;
        }
        
        assertThat(result).isEqualTo("client");
    }

    // Tests pour les collections

    @Test
    @DisplayName("Enum should work in collections")
    void enum_shouldWorkInCollections() {
        java.util.Set<UserRole> roles = java.util.Set.of(UserRole.CLIENT, UserRole.PROVIDER, UserRole.ADMIN);
        
        assertThat(roles).hasSize(3)
                         .contains(UserRole.CLIENT)
                         .contains(UserRole.PROVIDER)
                         .contains(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Enum should work in maps")
    void enum_shouldWorkInMaps() {
        java.util.Map<UserRole, String> roleDescriptions = java.util.Map.of(
            UserRole.CLIENT, "Utilisateur client",
            UserRole.PROVIDER, "Prestataire de services",
            UserRole.ADMIN, "Administrateur"
        );
        
        assertThat(roleDescriptions).hasSize(3)
                                   .containsEntry(UserRole.CLIENT, "Utilisateur client")
                                   .containsEntry(UserRole.PROVIDER, "Prestataire de services")
                                   .containsEntry(UserRole.ADMIN, "Administrateur");
    }

    // Tests pour les cas limites

    @Test
    @DisplayName("Enum should handle null comparison")
    void enum_shouldHandleNullComparison() {
        assertThat(UserRole.CLIENT).isNotNull();
        assertThat(UserRole.PROVIDER).isNotNull();
        assertThat(UserRole.ADMIN).isNotNull();
    }

    @Test
    @DisplayName("Enum should work with reflection")
    void enum_shouldWorkWithReflection() {
        Class<UserRole> enumClass = UserRole.class;
        
        assertThat(enumClass.isEnum()).isTrue();
        assertThat(enumClass.getEnumConstants()).hasSize(3)
                                               .contains(UserRole.CLIENT)
                                               .contains(UserRole.PROVIDER)
                                               .contains(UserRole.ADMIN);
    }
} 