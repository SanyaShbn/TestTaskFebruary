package com.example.testtaskfebruary.exception;

/**
 * Custom exception class for email already exists errors.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}