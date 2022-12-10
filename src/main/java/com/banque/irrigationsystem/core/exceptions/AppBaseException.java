package com.banque.irrigationsystem.core.exceptions;

public class AppBaseException extends RuntimeException {

    private final static String DEFAULT_ERROR_MESSAGE = "AppBaseException occurred, please contact Admin";

    public AppBaseException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AppBaseException(Throwable cause) {
        super(DEFAULT_ERROR_MESSAGE, cause);
    }

    public AppBaseException(String message) {
        super(message);
    }

    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
