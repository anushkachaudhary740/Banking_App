package com.cognologix.BankSystemApplicationAssignment.exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String exception){
        super(exception);
    }
}
