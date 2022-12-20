package com.cognologix.BankSystemApplicationAssignment.exceptions;

public class AccountAlreadyExistException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    public AccountAlreadyExistException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s is already exist with %s : %s", resourceName,fieldName,fieldValue));
    }
}
