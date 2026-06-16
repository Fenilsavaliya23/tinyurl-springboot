package com.FirstProject.TinyURL.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUrlNotFoundException(UrlNotFoundException ex, HttpServletRequest request) {
        // Create a custom, consistent JSON error response body
        ErrorResponseDto error = ErrorResponseDto.builder().
                statuscode(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        // Return a ResponseEntity with the 404 status and the custom body
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AliasAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAliasAlreadyExistsException(AliasAlreadyExistsException ex, HttpServletRequest request) {
        // Create a custom, consistent JSON error response body
        ErrorResponseDto error = ErrorResponseDto.builder().
                statuscode(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        // Return a ResponseEntity with the 404 status and the custom body
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
