package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.AccountNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.CustomerAlreadyExistException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.CustomerNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.responses.CustomerResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.CustomerServices;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CustomerServicesImplementation implements CustomerServices {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private Converter converter;
    @Autowired
   private Converter customerConverter;
    @Override
    public CustomerDto createNewCustomer(@Valid CustomerDto customerDto) {
        Customer customerDetails = this.customerConverter.customerDtoToModel(customerDto);
        Optional<Optional<Customer>> customer= Optional.of(customerRepo.findById(customerDto.getCustomerId()));
        if(customer.isPresent()){
            throw new CustomerAlreadyExistException("Customer","CustomerId",customerDetails.getCustomerId());
        }
        return this.customerConverter.customerModelToDto(customerDetails);
    }
    @Override
    public Optional<CustomerDto> getCustomerById(Integer customerId) {
        if(customerRepo.existsById(customerId)) {
            Customer customer = this.customerRepo.findById(customerId).get();
            return Optional.ofNullable(this.converter.customerModelToDto(customer));
        }
        else {
            throw new CustomerNotFoundException("Customer","Id",customerId);
        }
    }
    @Override
    public List<CustomerDto> findAllCustomerDetails() {
        List<Customer> list=this.customerRepo.findAll();
        List<CustomerDto> cusDtos =list.stream().map(e-> this.customerConverter.customerModelToDto(e))
                .collect(Collectors.toList());
        return cusDtos;
    }
    @Override
    public CustomerResponse updateCustomerDetails(CustomerDto customerDto,Integer customerId) {
        if(customerRepo.existsById(customerId)){
        Customer customerDetails = this.customerConverter.customerDtoToModel(customerDto);
        customerDetails.setCustomerId(customerId);
        this.customerRepo.save(customerDetails);
        return new CustomerResponse("Customer details updated successfully",true);
        }
        else {
            return new CustomerResponse("Customer Id does not exist",false);
        }

    }
    @Override
    public CustomerResponse deleteCustomer(Integer customerId) {
        if(customerRepo.existsById(customerId)) {
            this.customerRepo.deleteById(customerId);
            CustomerResponse customerResponse = new CustomerResponse("Customer details deleted successfully..", true);
            return customerResponse;
        }
        else {
            return new CustomerResponse("Customer Id does not exist",false);
        }
    }

}
