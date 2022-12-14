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
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
//        Map<String, String> resp = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            resp.put(fieldName, message);
//        });
//
//        return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
//   }
}
