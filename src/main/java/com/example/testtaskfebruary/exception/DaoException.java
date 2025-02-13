package com.example.testtaskfebruary.exception;

/**
 * Custom exception class for Data Access Object (DAO) related errors.
 */
public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}