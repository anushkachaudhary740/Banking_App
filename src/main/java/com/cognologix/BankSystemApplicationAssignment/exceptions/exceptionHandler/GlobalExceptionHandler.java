package com.cognologix.BankSystemApplicationAssignment.exceptions.exceptionHandler;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.GlobalErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.globalException.EmptyListException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.globalException.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.BalanceException;
import com.cognologix.BankSystemApplicationAssignment.responses.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<BaseResponse> resourceNotFoundExceptionHandler(Exception re) {
            log.throwing(re);
            return new ResponseEntity<>(new BaseResponse(re.getMessage(), false, GlobalErrors.RESOURCE_NOT_FOUND.getCode()),HttpStatus.NOT_FOUND);
        }
    @ExceptionHandler(EmptyListException.class)
    public ResponseEntity<BaseResponse> EmptyListException(Exception exception){
        log.throwing(exception);
        return new ResponseEntity<>(new BaseResponse(exception.getMessage(), false,GlobalErrors.EMPTY_LIST.getCode()), HttpStatus.NOT_FOUND);
    }

}
