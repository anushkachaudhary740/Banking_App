package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ApiResponse;
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
        return  ResponseEntity.ok(this.customerServices.findAllCustomerDetails());
    }
    @GetMapping("/get/{customerId}")
    public ResponseEntity<CustomerDto> findCustomerDetailsById( @PathVariable("customerId") Integer customerId){
        return ResponseEntity.ok(this.customerServices.getCustomerById(customerId));
    }
    @PutMapping("/update/{customerId}")
    public ResponseEntity<CustomerDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto, @PathVariable("customerId") Integer customerId){
        this.customerServices.updateCustomerDetails(customerDto,customerId);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);

    }
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<ApiResponse> deleteCustomerDetailsById(@PathVariable("customerId") Integer customerId){
        this.customerServices.deleteCustomer(customerId);
        return new ResponseEntity<>(new ApiResponse("Account deleted successfully..",true) ,HttpStatus.OK);
    }

}
