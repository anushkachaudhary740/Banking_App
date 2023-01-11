package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.responses.AllAccountsForACustomerResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.CustomerResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface CustomerServices {
    CustomerResponse createNewCustomer(CustomerDto customerDto);
    Optional<CustomerDto> getCustomerById(Integer customerId);
    AllAccountsForACustomerResponse getAllAccountsForACustomer(Integer customerId);
    List<CustomerDto> findAllCustomerDetails();
    CustomerResponse updateCustomerDetails(CustomerDto customerDto,Integer customerId);
    CustomerResponse deleteCustomer(Integer customerId);
}
