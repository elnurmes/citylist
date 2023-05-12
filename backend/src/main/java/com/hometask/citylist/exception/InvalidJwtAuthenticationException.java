package com.hometask.citylist.exception;

/**
 * @author Elnur Mammadov
 */
public class InvalidJwtAuthenticationException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
