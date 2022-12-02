package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public interface CustomerServices {
    CustomerDto createNewCustomer(CustomerDto customerDto);
    CustomerDto getCustomerById(Integer customerId);
    List<CustomerDto> findAllCustomerDetails();
    void updateCustomerDetails(CustomerDto customerDto, Integer customerId);
    void deleteCustomer(Integer customerId);

}
