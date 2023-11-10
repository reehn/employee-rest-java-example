package com.example.employeecrudjava.exceptionHandling;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpError missingRequiredFieldHandler(MethodArgumentNotValidException ex) {
        return HttpError.builder()
                .title(HttpStatus.BAD_REQUEST
                        .getReasonPhrase())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .detailedMessage(ex.getFieldError().getDefaultMessage()).build();
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpError fieldIsNullHandler(HttpMessageNotReadableException ex) {
        return HttpError.builder()
                .title(HttpStatus.BAD_REQUEST
                        .getReasonPhrase())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .detailedMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = { EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HttpError entityNotFoundHandler(EntityNotFoundException ex) {
        return HttpError.builder()
                .title(HttpStatus.NOT_FOUND
                        .getReasonPhrase())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .detailedMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public HttpError constrainViolationExceptionHandler(DataIntegrityViolationException ex) {
        return HttpError.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .title(HttpStatus.CONFLICT.getReasonPhrase())
                .detailedMessage(
                        getConstrainViolationExceptionMessage(ex))
                .build();
    }

    private String getConstrainViolationExceptionMessage(DataIntegrityViolationException ex) {
        return ex.getMessage().contains("Unique index") && ex.getMessage().contains("EMPLOYEE")
                ? "Employee with that email already exists"
                : ex.getMessage();
    }

}
