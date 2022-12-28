package com.cognologix.BankSystemApplicationAssignment.exceptions;

public class AccountNotFoundException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    public AccountNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName,fieldName,fieldValue));
    }}
