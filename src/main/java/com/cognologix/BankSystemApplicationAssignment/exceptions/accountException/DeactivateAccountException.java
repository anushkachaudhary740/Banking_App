package com.cognologix.BankSystemApplicationAssignment.exceptions.accountException;

public class DeactivateAccountException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    public DeactivateAccountException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s is DEACTIVATED with %s : %s", resourceName,fieldName,fieldValue));
    }
    public DeactivateAccountException(){
        super();
    }
}
