package com.automotive.customer.exception;

public class VehicleDoesNotExistException extends Exception{

    public VehicleDoesNotExistException(String message)
    {
        super(message);
    }
}
