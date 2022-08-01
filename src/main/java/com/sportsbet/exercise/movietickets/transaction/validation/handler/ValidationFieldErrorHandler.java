package com.sportsbet.exercise.movietickets.transaction.validation.handler;

import com.sportsbet.exercise.movietickets.transaction.validation.response.FieldValidationViolation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validation field error handler for ConstraintViolationException & MethodArgumentNotValidException
 */
@ControllerAdvice
public class ValidationFieldErrorHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    List<FieldValidationViolation> onConstraintValidationException(
            ConstraintViolationException e) {
        final List<FieldValidationViolation> violations = e.getConstraintViolations().stream()
                .map(violation ->
                        new FieldValidationViolation(violation.getPropertyPath().toString()
                                , violation.getMessage()))
                .collect(Collectors.toList());

        return violations;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    List<FieldValidationViolation> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        final List<FieldValidationViolation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error ->
                        new FieldValidationViolation(error.getField()
                                , error.getDefaultMessage()))
                .collect(Collectors.toList());

        return violations;
    }
}
