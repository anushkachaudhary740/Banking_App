package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.service.services.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerServices customerServices;
    @PostMapping("/create")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto){
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
    public ResponseEntity<?> updateAccountDetails(@RequestBody CustomerDto customerDto, @PathVariable("customerId") Integer customerId){
        this.customerServices.updateCustomerDetails(customerDto,customerId);
        return new ResponseEntity<>("Customer Details updated successfully..", HttpStatus.OK);

    }
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<String> deleteCustomerDetailsById(@PathVariable("customerId") Integer customerId){
        this.customerServices.deleteCustomer(customerId);
        return new ResponseEntity<>("Account deleted successfully..", HttpStatus.OK);
    }

}
