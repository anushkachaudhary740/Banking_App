package com.cognologix.BankSystemApplicationAssignment.exceptions.exceptionHandler;

import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.TransactionErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.BalanceException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.InSufficientBalanceException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.TransactionsNotAvailableException;
import com.cognologix.BankSystemApplicationAssignment.responses.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class TransactionExceptionHandler {
    @ExceptionHandler(InSufficientBalanceException.class)
    public ResponseEntity<BaseResponse> InSufficientBalanceException(Exception exception){
        log.throwing(exception);
        return new ResponseEntity<>(new BaseResponse(exception.getMessage(), false, TransactionErrors.INSUFFICIENT_BALANCE.getCode()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BalanceException.class)
    public ResponseEntity<BaseResponse> BalanceException(Exception exception){
        log.throwing(exception);
        return new ResponseEntity<>(new BaseResponse(exception.getMessage(), false,TransactionErrors.BALANCE_EXCEPTION.getCode()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TransactionsNotAvailableException.class)
    public ResponseEntity<BaseResponse> TransactionsNotAvailableException(Exception exception){
        log.throwing(exception);
        BaseResponse baseResponse=new BaseResponse(exception.getMessage(), false,TransactionErrors.TRANSACTIONS_NOT_AVAILABLE.getCode());
        return new ResponseEntity<>(baseResponse,HttpStatus.NOT_FOUND);
    }
}
