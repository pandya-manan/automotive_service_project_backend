package com.automotive.service.controller;

import com.automotive.service.exception.*;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.automotive.service.entity.*;
import java.time.LocalDateTime;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WorkOrderException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(WorkOrderException ex) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleOptimistic(OptimisticLockException ex) {
        return build(HttpStatus.CONFLICT, "Conflict", "Concurrent modification detected. Retry.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse(ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Validation Failed", msg);
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message) {
        ErrorResponse body = new ErrorResponse(LocalDateTime.now(), status.value(), error, message, "");
        return ResponseEntity.status(status).body(body);
    }
}
