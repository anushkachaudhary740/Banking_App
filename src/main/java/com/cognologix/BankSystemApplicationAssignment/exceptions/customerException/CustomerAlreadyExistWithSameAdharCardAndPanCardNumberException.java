package com.cognologix.BankSystemApplicationAssignment.exceptions.customerException;

public class CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException extends RuntimeException{
    String resourceName;
    String fieldName;
    String secondFieldName;
    String fieldValues;
    String secondFieldValues;
    public CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException(String resourceName, String fieldName,String fieldValues,String secondFieldName,String secondFieldValues) {
        super(String.format("%s is already exist with %s :%s or with %s: %s", resourceName,fieldName,fieldValues,secondFieldName,secondFieldValues));
    }

    public CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException() {
        super();
    }
}
