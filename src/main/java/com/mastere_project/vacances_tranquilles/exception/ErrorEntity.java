package com.mastere_project.vacances_tranquilles.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorEntity {
    private String code;
    private String message;
}
