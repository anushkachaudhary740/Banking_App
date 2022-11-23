package com.cognologix.BankSystemApplicationAssignment.converter.customerconverter;

import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerModelToDtoConverter {
    @Autowired
    private ModelMapper modelMapper;

    public CustomerDto modelToDto(Customer customer) {
        CustomerDto cdto = this.modelMapper.map(customer, CustomerDto.class);
        return cdto;
    }
}
