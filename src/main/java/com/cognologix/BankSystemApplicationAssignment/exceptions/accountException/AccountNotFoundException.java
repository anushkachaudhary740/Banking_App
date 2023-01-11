package com.cognologix.BankSystemApplicationAssignment.exceptions.accountException;


public class AccountNotFoundException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    public AccountNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName,fieldName,fieldValue));
    }
    public AccountNotFoundException(){
        super();
    }
}
