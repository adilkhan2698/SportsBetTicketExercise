package com.sportsbet.exercise.movietickets.transaction.validation.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Message object for field validation violations
 */
@Getter @Setter
public class FieldValidationViolation {
    private String fieldName;
    private String message;

    public FieldValidationViolation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
