package com.hometask.citylist.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Elnur Mammadov
 */
@ControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingControllerAdvice.class);

    private final ObjectMapper objectMapper;

    public ErrorHandlingControllerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static String exceptionToError(Throwable exception) {
        String errorName = camelCaseToSnakeCase(exception.getClass().getSimpleName()).toUpperCase(Locale.ENGLISH);
        if (errorName.toUpperCase(Locale.ENGLISH).endsWith("_EXCEPTION")) {
            errorName = errorName.substring(0, errorName.length() - "_EXCEPTION".length());
        }
        return errorName;
    }

    public static String camelCaseToSnakeCase(String message) {
        return message.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase(Locale.ENGLISH);
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private static class ExceptionWrapper {
        ExceptionWrapper(Throwable exception) {
            this(exception.getMessage(), exceptionToError(exception));
        }
        ExceptionWrapper(Throwable exception, String message) {
            this(message, exceptionToError(exception));
        }
        ExceptionWrapper(String message, String error) {
            this.message = message;
            this.error = error;
        }
        @JsonProperty("status")
        public String getStatus() {
            return "FAILURE";
        }
        @JsonProperty("error")
        public String getError() {
            return error;
        }
        @JsonProperty("message")
        public String getMessage() {
            return message;
        }
        @JsonProperty
        public Boolean getResult() {
            return Boolean.FALSE;
        }
        private final String message;
        private final String error;
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity defaultException(Throwable e) {
        LOGGER.error("Handled internal server error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionWrapper(e, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity authenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionWrapper(e, e.getMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionWrapper(e, "Unable to parse message"));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity notFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity badRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionWrapper(e));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity accessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(e, HttpStatus.FORBIDDEN.getReasonPhrase()));
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private static final class ValidationErrors extends ExceptionWrapper {
        private ValidationErrors(Map<String, String> errors, Exception e) {
            super(e, "Validation errors");
            this.errors = errors;
        }
        @JsonProperty("error")
        public String getError() {
            return "VALIDATION_ERROR";
        }
        @JsonProperty
        public Map<String, String> getErrors() {
            return errors;
        }
        private Map<String, String> errors;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity argumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }
        ValidationErrors validationErrors = new ValidationErrors(errors, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity constraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
            errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }
        ValidationErrors validationErrors = new ValidationErrors(errors, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity validationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionWrapper(e));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionWrapper(e));
    }
}
