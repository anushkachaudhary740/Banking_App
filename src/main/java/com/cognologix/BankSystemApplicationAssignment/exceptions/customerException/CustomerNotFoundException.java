package com.cognologix.BankSystemApplicationAssignment.exceptions.customerException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CustomerNotFoundException extends RuntimeException{
        String resourceName;
        String fieldName;
        long fieldValue;
        public CustomerNotFoundException(String resourceName, String fieldName, long fieldValue) {
            super(String.format("%s not found with %s : %s", resourceName,fieldName,fieldValue));
        }
        public CustomerNotFoundException() {
                super();
        }
}
