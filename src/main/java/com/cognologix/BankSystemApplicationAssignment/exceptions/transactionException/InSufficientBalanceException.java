package com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class InSufficientBalanceException extends RuntimeException{
    public InSufficientBalanceException(String message) {
        super(message);
    }
    public InSufficientBalanceException(){
        super();
    }


}

