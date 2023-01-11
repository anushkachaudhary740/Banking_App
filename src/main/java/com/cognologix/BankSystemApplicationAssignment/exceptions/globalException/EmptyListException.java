package com.cognologix.BankSystemApplicationAssignment.exceptions.globalException;

public class EmptyListException extends RuntimeException{
        public EmptyListException(String exception) {
            super(exception);
        }

    public EmptyListException() {
            super();
    }
}



