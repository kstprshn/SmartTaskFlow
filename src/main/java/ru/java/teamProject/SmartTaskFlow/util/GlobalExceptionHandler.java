package ru.java.teamProject.SmartTaskFlow.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}
