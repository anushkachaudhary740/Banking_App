package com.cognologix.BankSystemApplicationAssignment.exceptions.accountException;

public class AccountStatusException extends RuntimeException{
    public AccountStatusException() {
            super(String.format("Account is already activated "));
        }


    }
