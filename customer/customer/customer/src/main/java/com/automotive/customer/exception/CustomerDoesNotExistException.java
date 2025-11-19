package com.automotive.customer.exception;

public class CustomerDoesNotExistException extends Exception{

    public CustomerDoesNotExistException(String message)
    {
        super(message);
    }
}
