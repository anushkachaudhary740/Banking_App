package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
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
    public CustomerServicesImplementation(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private Converter converter;
    @Autowired
   private Converter customerConverter;
    Random random=new Random();
    @Override
    public CustomerDto createNewCustomer(@Valid CustomerDto customerDto) {
        Customer customerDetails = this.customerConverter.customerDtoToModel(customerDto);
        this.customerRepo.save(customerDetails);
//        Account account=new Account();
//        account.setCustomerId(customerDto.getCustomerId());
//        account.setAccountNumber(account.getAccountNumber());
//        account.setBankName(customerDto.getBankName());
//        account.setAccountHolderName(customerDto.getCustomerName());
//        account.setTypeOfAccount(customerDto.getTypeOfAccount());
//        account.setTypeOfAccount(customerDto.getTypeOfAccount());
//        account.setTotalAmount(0.0);
        return this.customerConverter.customerModelToDto(customerDetails);
    }
    @Override
    public Optional<CustomerDto> getCustomerById(Integer customerId) {
        Customer customer=this.customerRepo.findById(customerId).orElseThrow(()->new ResourceNotFoundException("Customer","Id",customerId));
        return Optional.ofNullable(this.customerConverter.customerModelToDto(customer));

    }
    @Override
    public List<CustomerDto> findAllCustomerDetails() {
        List<Customer> list=this.customerRepo.findAll();
        List<CustomerDto> cusDtos =list.stream().map(e-> this.customerConverter.customerModelToDto(e))
                .collect(Collectors.toList());
        return cusDtos;
    }
    @Override
    public CustomerDto updateCustomerDetails(CustomerDto customerDto) {
        Customer customerDetails = this.customerConverter.customerDtoToModel(customerDto);
        //customerDetails.setCustomerId(customerId);
        this.customerRepo.save(customerDetails);
        return this.customerConverter.customerModelToDto(customerDetails);

    }
    @Override
    public CustomerResponse deleteCustomer(Integer customerId) {
        this.customerRepo.deleteById(customerId);
        CustomerResponse customerResponse=new CustomerResponse("Account deleted successfully..",true);
        return customerResponse;
    }

}
