package com.cognologix.BankSystemApplicationAssignment.exceptions.accountException;

public class AccountNotAvailableException extends RuntimeException {
    String resourceName;
    String fieldName;
    long fieldValue;
    public AccountNotAvailableException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("no any %s available with this customer %s : %s", resourceName,fieldName,fieldValue));
    }
    public AccountNotAvailableException(){
        super();
    }

}
