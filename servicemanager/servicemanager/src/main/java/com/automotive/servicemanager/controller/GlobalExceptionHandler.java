package com.automotive.servicemanager.controller;

import com.automotive.servicemanager.exception.ServiceManagerDoesNotExistException;
import com.automotive.servicemanager.exception.VehicleDoesNotExistException;
import com.automotive.servicemanager.exception.WorkOrderDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceManagerDoesNotExistException.class)
    public ResponseEntity<Map<String, String>> handleServiceManagerNotFound(ServiceManagerDoesNotExistException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Service Manager Not Found");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(VehicleDoesNotExistException.class)
    public ResponseEntity<Map<String, String>> handleVehicleNotFound(VehicleDoesNotExistException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Vehicle does not exist");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(WorkOrderDoesNotExistException.class)
    public ResponseEntity<Map<String, String>> handleWorkOrderNotFound(WorkOrderDoesNotExistException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Work Order does not exist");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid Operation");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid Argument");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, String>> handleGeneralSQLExceptions(SQLException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "SQL Error");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
