package com.cognologix.BankSystemApplicationAssignment.exceptions;

public class InSufficientBalanceException extends RuntimeException{
    public InSufficientBalanceException(String exception) {
        super(exception);
    }
}

