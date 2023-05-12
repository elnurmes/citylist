package com.hometask.citylist.exception;

/**
 * @author Elnur Mammadov
 */
public class BadRequestException extends IllegalArgumentException {
    private static final long serialVersionUID = 6945582226327180639L;
    public BadRequestException(String message) {
        super(message);
    }
}
