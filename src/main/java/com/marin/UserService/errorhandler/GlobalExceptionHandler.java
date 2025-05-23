package com.marin.UserService.errorhandler;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global error handler of all exception that may present during the execution of this API caused by calls to any end point.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manages general unchecked exceptions that might present during the execution of the app.
     * This only prevents the app from crashing
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex , HttpServletRequest request){
        System.err.println(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something unexpected happened on the server");
    }

    /**
     * Manages exceptions created by the database and mosty be risen by trying to persist duplicated entries that are restricted by unique
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        System.err.println(ex);
        return ResponseEntity.badRequest().body("Validation error: " + ex.getMessage());
    }

    /**
     * Manages exceptions created by received not valid DTOs / Entities as body parameters in the endpoints that require a body param.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Manages exceptions risen by receive a bad body in the endpoints that require one.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){

        return ResponseEntity.badRequest().body("Request body missing or malformed");
    }

    /**
     * Manages exceptions risen by trying to log in with bad credentials into the auth controller.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
    }

    /**
     * Global error handler for general exceptions that might rise during the app.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Operation(summary = "Handles Global Exceptions", description = "Returns custom error messages")
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + ex.getMessage());
    }

}
