package com.cognologix.BankSystemApplicationAssignment.exceptions;

    public class CustomerNotFoundException extends RuntimeException{
        String resourceName;
        String fieldName;
        long fieldValue;
        public CustomerNotFoundException(String resourceName, String fieldName, long fieldValue) {
            super(String.format("%s not found with %s : %s", resourceName,fieldName,fieldValue));
        }

}
