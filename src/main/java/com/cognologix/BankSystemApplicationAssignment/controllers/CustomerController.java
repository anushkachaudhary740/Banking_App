package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.responses.BaseResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.CustomerResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerServices customerServices;
    @PostMapping("/create")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto){
        customerServices.createNewCustomer(customerDto);
        return new ResponseEntity<CustomerDto>(customerDto, HttpStatus.CREATED);
    }
    @GetMapping("/get")
    public ResponseEntity<List<CustomerDto>> getCustomerDetails() {
        List<CustomerDto> customerDto=this.customerServices.findAllCustomerDetails();
        HttpStatus httpStatus=customerDto.size()>0?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return  new ResponseEntity<>(customerDto,httpStatus);
    }
    @GetMapping("/get/{customerId}")
    public ResponseEntity<Optional<CustomerDto>> findCustomerDetailsById(@PathVariable("customerId") Integer customerId){
        Optional<CustomerDto> customerDto=this.customerServices.getCustomerById(customerId);
        HttpStatus httpStatus=customerDto.isPresent()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(customerDto,httpStatus);
    }
    @PutMapping("/update/{customerId}")
    public ResponseEntity<CustomerResponse> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto, @PathVariable("customerId") Integer customerId){
        CustomerResponse customerResponse=this.customerServices.updateCustomerDetails(customerDto,customerId);
        //HttpStatus httpStatus=customerResponse.getSuccess()==true?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(customerResponse,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<CustomerResponse> deleteCustomerDetailsById(@PathVariable("customerId") Integer customerId){
        CustomerResponse customerResponse=this.customerServices.deleteCustomer(customerId);
        HttpStatus httpStatus=customerResponse.getSuccess()==true?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(customerResponse ,httpStatus);
    }

}
