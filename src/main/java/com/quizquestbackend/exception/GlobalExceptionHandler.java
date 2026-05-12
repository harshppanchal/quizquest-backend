package com.quizquestbackend.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.quizquestbackend.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1️⃣ Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error ->
              errors.put(error.getField(), error.getDefaultMessage())
          );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        false,
                        "Validation failed",
                        errors
                ));
    }

    // 2️⃣ Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                ));
    }

    // 3️⃣ Duplicate resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(
            DuplicateResourceException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                ));
    }

    // 4️⃣ Unauthorized access
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                ));
    }

   

    // 5 Final fallback (DO NOT expose details)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        false,
                        "Internal server error",
                        null
                ));
    }
}