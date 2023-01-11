package com.cognologix.BankSystemApplicationAssignment.exceptions.exceptionHandler;

import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.AccountErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountAlreadyExistException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountNotAvailableException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountStatusException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.DeactivateAccountException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.IllegalTypeOfAccountException;
import com.cognologix.BankSystemApplicationAssignment.responses.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class AccountExceptionHandler {
    @ExceptionHandler(AccountAlreadyExistException.class)
    public ResponseEntity<BaseResponse> accountAlreadyExistExceptionHandler(AccountAlreadyExistException acc){
        log.throwing(acc);
        BaseResponse baseResponse = new BaseResponse(acc.getMessage(), false,AccountErrors.ACCOUNT_ALREADY_EXIST.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<BaseResponse> accountNotFoundException(AccountNotFoundException account){
        log.throwing(account);
        BaseResponse baseResponse = new BaseResponse(account.getMessage(), false, AccountErrors.ACCOUNT_NOT_FOUND.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(IllegalTypeOfAccountException.class)
    public ResponseEntity<BaseResponse> IllegalTypeOfAccountException(IllegalTypeOfAccountException account){
        log.throwing(account);
        BaseResponse baseResponse = new BaseResponse(account.getMessage(), false,AccountErrors.ILLEGAL_TYPE_OF_ACCOUNT.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(DeactivateAccountException.class)
    public ResponseEntity<BaseResponse> DeactivateAccountException(DeactivateAccountException account){
        log.throwing(account);
        BaseResponse baseResponse = new BaseResponse(account.getMessage(), false,AccountErrors.DEACTIVATE_ACCOUNT.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccountNotAvailableException.class)
    public ResponseEntity<BaseResponse> AccountNotAvailableException(AccountNotAvailableException account){
        log.throwing(account);
        BaseResponse baseResponse = new BaseResponse(account.getMessage(), false,AccountErrors.ACCOUNT_NOT_AVAILABLE.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<BaseResponse> AccountStatusException(AccountStatusException account){
        log.throwing(account);
        BaseResponse baseResponse = new BaseResponse(account.getMessage(), false,AccountErrors.ILLEGALE_TYPE_OF_ACCOUNT_STATUS.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }
}
