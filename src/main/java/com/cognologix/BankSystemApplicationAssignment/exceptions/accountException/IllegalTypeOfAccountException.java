package com.cognologix.BankSystemApplicationAssignment.exceptions.accountException;

public class IllegalTypeOfAccountException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    public IllegalTypeOfAccountException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s account %s : %s", resourceName,fieldName,fieldValue));
    }
    public IllegalTypeOfAccountException(){
        super();
    }
}
