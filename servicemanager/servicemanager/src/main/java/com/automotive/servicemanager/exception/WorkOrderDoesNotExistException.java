package com.automotive.servicemanager.exception;

public class WorkOrderDoesNotExistException extends Exception {
    public WorkOrderDoesNotExistException(String message) {
        super(message);
    }
}