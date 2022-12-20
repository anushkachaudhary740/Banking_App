package com.cognologix.BankSystemApplicationAssignment.exceptions;
import com.cognologix.BankSystemApplicationAssignment.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<BaseResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException re) {
            String message = re.getMessage();
            BaseResponse baseResponse = new BaseResponse(message, false);
            return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.NOT_FOUND);
        }
        @ExceptionHandler(AccountAlreadyExistException.class)
        public ResponseEntity<BaseResponse> accountAlreadyExistExceptionHandler(AccountAlreadyExistException acc){
            String message=acc.getMessage();
            BaseResponse baseResponse = new BaseResponse(message, false);
            return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.NOT_FOUND);

        }
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<BaseResponse> customerNotFoundException(CustomerNotFoundException cus){
        String message=cus.getMessage();
        BaseResponse baseResponse = new BaseResponse(message, false);
        return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<BaseResponse> customerAlreadyExistException(CustomerAlreadyExistException cus){
        String message=cus.getMessage();
        BaseResponse baseResponse = new BaseResponse(message, false);
        return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<BaseResponse> accountNotFoundException(AccountNotFoundException account){
        String message=account.getMessage();
        BaseResponse baseResponse = new BaseResponse(message, false);
        return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.NOT_FOUND);

    }
}
