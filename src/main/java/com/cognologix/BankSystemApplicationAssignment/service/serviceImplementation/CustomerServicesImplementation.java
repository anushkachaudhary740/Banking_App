package com.cognologix.BankSystemApplicationAssignment.service.serviceImplementation;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.enums.customerEnums.CustomerMsgEnum;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.CustomerErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountNotAvailableException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.responses.AllAccountsForACustomerResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.CustomerResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.CustomerServices;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CustomerServicesImplementation implements CustomerServices {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private Converter converter;
    @Autowired
    private Converter customerConverter;
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public CustomerResponse createNewCustomer(CustomerDto customerDto) {
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        Customer newCustomer = customerRepo.findByCustomerAadharCardNumberAndCustomerPanCardNumber(customer.getCustomerAadharCardNumber(),
                customer.getCustomerPanCardNumber());
        if (newCustomer != null) {
            log.error("Customer is already exist with Aadhar :"+customer.getCustomerAadharCardNumber()+" or with PanCardNumber: "+customer.getCustomerPanCardNumber());
            throw new CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException("Customer", "AdharNumber", newCustomer.getCustomerAadharCardNumber(), "PanCardNumber", newCustomer.getCustomerPanCardNumber());
        }
        Customer customerCreated = customerRepo.save(customer);
        CustomerDto newCust = customerConverter.customerModelToDto(customerCreated);
        CustomerResponse customerResponse = new CustomerResponse(CustomerMsgEnum.CREATE_CUSTOMER.getMessage(), true, newCust);
        log.info(customerResponse.getMessage());
        return customerResponse;
    }

    @Override
    public Optional<CustomerDto> getCustomerById(Integer customerId) {
        if (customerRepo.existsById(customerId)) {
            Customer customer = this.customerRepo.findById(customerId).get();
            log.info(CustomerMsgEnum.PRINT_CUSTOMER.getMessage());
            return Optional.ofNullable(this.converter.customerModelToDto(customer));
        } else {
            log.error(CustomerErrors.CUSTOMER_NOT_FOUND +" with Id: "+customerId);
            throw new CustomerNotFoundException("Customer", "Id", customerId);
        }
    }

    @Override
    public AllAccountsForACustomerResponse getAllAccountsForACustomer(Integer customerId) {
        if (customerRepo.existsById(customerId)) {
            List<Account> accounts = accountRepo.getAllAccountsForACustomer(customerId);
            if (accounts.isEmpty()) {
                log.error(CustomerErrors.ACCOUNT_NOT_AVAILABLE_FOR_CUSTOMER.getMessage()+customerId);
                throw new AccountNotAvailableException("Account", "Id", customerId);
            }
            System.out.println(accounts);
            AllAccountsForACustomerResponse allAccountsForACustomerResponse = new AllAccountsForACustomerResponse(CustomerMsgEnum.ALL_ACCOUNTS_FOR_CUSTOMER.getMessage(), true, accounts);
            log.info(allAccountsForACustomerResponse.getMessage());
            return allAccountsForACustomerResponse;
        } else {
            log.error(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage());
            return new AllAccountsForACustomerResponse(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage(), false, null);
        }
    }

    @Override
    public List<CustomerDto> findAllCustomerDetails() {
        List<Customer> list = this.customerRepo.findAll();
        List<CustomerDto> cusDtos = list.stream().map(e -> this.customerConverter.customerModelToDto(e))
                .collect(Collectors.toList());
        log.info(CustomerMsgEnum.CUSTOMER_DETAILS.getMessage());
        return cusDtos;
    }

    @Override
    public CustomerResponse updateCustomerDetails(CustomerDto customerDto, Integer customerId) {
        if (customerRepo.existsById(customerId)) {
            Customer customerDetails = this.customerConverter.customerDtoToModel(customerDto);
            customerDetails.setCustomerId(customerId);
            this.customerRepo.save(customerDetails);
            log.info(CustomerMsgEnum.UPDATE_CUSTOMER.getMessage());
            return new CustomerResponse(CustomerMsgEnum.UPDATE_CUSTOMER.getMessage(), true, customerDto);
        } else {
            log.error(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage());
            return new CustomerResponse(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage(), false, customerDto);
        }
    }
    @Override
    public CustomerResponse deleteCustomer(Integer customerId) {
        if (customerRepo.existsById(customerId)) {
            this.customerRepo.deleteById(customerId);
            CustomerResponse customerResponse = new CustomerResponse(CustomerMsgEnum.DELETE_CUSTOMER.getMessage(), true, null);
            log.info(CustomerMsgEnum.DELETE_CUSTOMER.getMessage());
            return customerResponse;
        } else {
            log.error(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage());
            return new CustomerResponse(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage(), false, null);
        }
    }

}
