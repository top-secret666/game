package ru.vitrailclinic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex, WebRequest request) {
        // CWE-778: log resource-not-found events to allow detection of enumeration attacks
        log.warn("Resource not found [{}]: {}", request.getDescription(false), ex.getMessage());
        ApiError err = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).collect(Collectors.toList());
        // CWE-778: log validation failures to detect injection and fuzzing attempts
        log.warn("Validation failure [{}]: fields=[{}]",
                request.getDescription(false),
                ex.getBindingResult().getFieldErrors().stream()
                        .map(fe -> fe.getField()).collect(Collectors.joining(", ")));
        ApiError err = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, WebRequest request) {
        // CWE-778: log full exception server-side for incident response and forensic analysis
        log.error("Unhandled exception for request [{}]", request.getDescription(false), ex);
        // CWE-209 fix: never expose internal exception messages (stack traces, DB schema, etc.) to the client
        ApiError err = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal error",
                List.of("An unexpected error occurred. Please contact support."));
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
