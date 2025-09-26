package com.automotive.servicemanager.exception;

public class ServiceManagerDoesNotExistException extends Exception {
    public ServiceManagerDoesNotExistException(String message) {
        super(message);
    }
}