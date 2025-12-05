package com.jvictornascimento.accessmanager.controller.exceptions;

import com.jvictornascimento.accessmanager.service.exceptions.EmailAlreadyExistsException;
import com.jvictornascimento.accessmanager.service.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.*;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> methodNotAllowed(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        var error = GENERIC_METHOD_NOT_ALLOW.params(request.getMethod()).getMassage();
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        var standardError = new StandardError( Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> internalServerError(Exception e, HttpServletRequest request) {
        var error = GENERIC_EXCEPTION.getMassage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        var standardError = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    //AccessManager Errors

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> userNotFound(UserNotFoundException e, HttpServletRequest request) {
        var error = USER_NOT_FOUND.getMassage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        var standardError = new StandardError( Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<StandardError> emailAlreadyExists(EmailAlreadyExistsException e, HttpServletRequest request) {
        var error = USER_EMAIL_ALREADY_EXISTS.getMassage();
        HttpStatus status = HttpStatus.CONFLICT;
        var standardError = new StandardError( Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldValidationError> userRequiredFields(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> fieldsError = new HashMap<>();

        var error = USER_REQUIRED_FIELDS.getMassage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        e.getBindingResult().getFieldErrors().forEach(fieldError -> fieldsError.put( fieldError.getField(), fieldError.getDefaultMessage()));
        var fieldValidationError  = new FieldValidationError( Instant.now(), status.value(), error, fieldsError, request.getRequestURI());
        return ResponseEntity.status(status).body(fieldValidationError);
    }

}
