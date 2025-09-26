package com.automotive.servicemanager.exception;

public class VehicleDoesNotExistException extends Exception {
    public VehicleDoesNotExistException(String message) {
        super(message);
    }
}