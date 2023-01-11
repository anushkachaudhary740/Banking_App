package com.cognologix.BankSystemApplicationAssignment.exceptions.exceptionHandler;

import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.CustomerErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerAlreadyExistException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.responses.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CustomerExceptionHandler {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<BaseResponse> customerNotFoundException(CustomerNotFoundException cus){
        log.throwing(cus);
        BaseResponse baseResponse = new BaseResponse(cus.getMessage(), false, CustomerErrors.CUSTOMER_NOT_FOUND.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<BaseResponse> customerAlreadyExistException(CustomerAlreadyExistException cus){
        log.error(cus);
        BaseResponse baseResponse = new BaseResponse(cus.getMessage(), false,CustomerErrors.CUSTOMER_ALREADY_EXIST.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException.class)
    public ResponseEntity<BaseResponse> customerAlreadyExistWithSameAdharNumberException(CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException cus){
        log.error(cus);
        BaseResponse baseResponse = new BaseResponse(cus.getMessage(), false,CustomerErrors.CUSTOMER_ALREADY_EXIST.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);

    }
}
