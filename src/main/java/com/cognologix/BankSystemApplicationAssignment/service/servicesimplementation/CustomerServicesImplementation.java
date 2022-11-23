package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.customerconverter.CustomerDtoToModelConverter;
import com.cognologix.BankSystemApplicationAssignment.converter.customerconverter.CustomerModelToDtoConverter;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.service.services.CustomerServices;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServicesImplementation implements CustomerServices {
    public CustomerServicesImplementation(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private CustomerDtoToModelConverter customerDtoToModelConverter;
    @Autowired
    private CustomerModelToDtoConverter customerModelToDtoConverter;
    @Override
    public CustomerDto createNewCustomer(CustomerDto customerDto) {
        Customer customerDetails = this.customerDtoToModelConverter.dtoToModel(customerDto);
        Customer cus = this.customerRepo.save(customerDetails);
        return this.customerModelToDtoConverter.modelToDto(cus);

    }
    @Override
    public CustomerDto getCustomerById(Integer customerId) {
        Customer customer=this.customerRepo.findById(customerId).orElseThrow(()->new ResourceNotFoundException("Customer","Id",customerId));
        return this.customerModelToDtoConverter.modelToDto(customer);

    }
    @Override
    public List<CustomerDto> findAllCustomerDetails() {
        List<Customer> list=this.customerRepo.findAll();
        List<CustomerDto> cusDtos =list.stream().map(e-> this.customerModelToDtoConverter.modelToDto(e))
                .collect(Collectors.toList());
        return cusDtos;
    }
    @Override
    public void updateCustomerDetails(CustomerDto customerDto, Integer customerId) {
        Customer customerDetails = this.customerDtoToModelConverter.dtoToModel(customerDto);
        customerDetails.setCustomerId(customerId);
        this.customerRepo.save(customerDetails);
        this.customerModelToDtoConverter.modelToDto(customerDetails);
    }
    @Override
    public void deleteCustomer(Integer customerId) {
        this.customerRepo.deleteById(customerId);
    }

}
