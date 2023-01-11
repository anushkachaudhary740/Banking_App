package com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException;

public class TransactionsNotAvailableException extends RuntimeException {
    String resourceName;
    String fieldName;
    long fieldValue;
    public TransactionsNotAvailableException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("no any %s available with this account %s : %s", resourceName,fieldName,fieldValue));
    }


}
