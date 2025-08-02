package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un service n'est pas trouvé en base.
 */
public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String message) {
        super(message);
    }
}