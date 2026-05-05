package com.pedro.jobtrackapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ApiError handleResponseStatusException(ResponseStatusException exception, HttpServletRequest request) {
        int statusCode = exception.getStatusCode().value();

        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        return new ApiError(
                LocalDateTime.now(),
                statusCode,
                httpStatus != null ? httpStatus.getReasonPhrase() : "Error",
                exception.getReason(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .findFirst().orElse("Validation Error");
        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }
}
