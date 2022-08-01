package com.sportsbet.exercise.movietickets.transaction.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom error for transaction not found
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Transaction Not Found")
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
