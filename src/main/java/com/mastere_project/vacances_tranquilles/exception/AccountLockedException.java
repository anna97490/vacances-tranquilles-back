package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un compte utilisateur est temporairement bloqué suite
 * à trop de tentatives de connexion échouées.
 */
public class AccountLockedException extends RuntimeException {
    /**
     * Construit une nouvelle exception AccountLockedException avec le message
     * spécifié.
     *
     * @param message le message détaillant la raison du blocage
     */
    public AccountLockedException(String message) {
        super(message);
    }
}