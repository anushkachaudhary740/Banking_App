package com.cognologix.BankSystemApplicationAssignment.converter.customerconverter;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoToModelConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Customer dtoToModel(CustomerDto customerDto) {
        Customer dtoCustomer = this.modelMapper.map(customerDto, Customer.class);
        return dtoCustomer;
    }
}
