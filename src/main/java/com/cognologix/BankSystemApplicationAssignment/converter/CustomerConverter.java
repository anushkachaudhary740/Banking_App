package com.cognologix.BankSystemApplicationAssignment.converter;

import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter {
    @Autowired
    private ModelMapper modelMapper;
    //dto to model converter
    public Customer dtoToModel(CustomerDto customerDto) {
        Customer dtoCustomer = this.modelMapper.map(customerDto, Customer.class);
        return dtoCustomer;
    }
    // model to dto converter
    public CustomerDto modelToDto(Customer customer) {
        CustomerDto cdto = this.modelMapper.map(customer, CustomerDto.class);
        return cdto;
    }
}
