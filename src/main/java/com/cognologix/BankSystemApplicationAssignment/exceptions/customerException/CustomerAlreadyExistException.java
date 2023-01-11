package com.cognologix.BankSystemApplicationAssignment.exceptions.customerException;

public class CustomerAlreadyExistException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    public CustomerAlreadyExistException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s is already exist with %s : %s", resourceName,fieldName,fieldValue));
    }
    public CustomerAlreadyExistException(){
        super();
    }

}
