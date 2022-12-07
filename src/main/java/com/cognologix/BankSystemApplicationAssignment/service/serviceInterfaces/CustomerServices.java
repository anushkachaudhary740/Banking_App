package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface CustomerServices {
    CustomerDto createNewCustomer(CustomerDto customerDto);
    Optional<CustomerDto> getCustomerById(Integer customerId);
    List<CustomerDto> findAllCustomerDetails();
    CustomerDto updateCustomerDetails(CustomerDto customerDto);
    void deleteCustomer(Integer customerId);

}
