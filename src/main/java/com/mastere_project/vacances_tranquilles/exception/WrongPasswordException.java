package com.mastere_project.vacances_tranquilles.exception;


public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException(String message) {
        super(message);
    }
}
