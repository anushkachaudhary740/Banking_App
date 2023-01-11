package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.enums.customerEnums.CustomerMsgEnum;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.CustomerErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.responses.AllAccountsForACustomerResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.CustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServicesTest {
    private CustomerDto customerDto;
    @Autowired
    private CustomerServices customerServices;
    @MockBean
    private CustomerRepo customerRepo;
    @Autowired
    private Converter customerConverter;
    private Customer customer;
    @Autowired
    private AccountRepo accountRepo;

    @BeforeEach
    void setUp() {
        customerDto = CustomerDto.builder()
                .customerId(1)
                .customerName("Anu Chaudhary")
                .customerGender("female")
                .customerMobileNumber("1234567890")
                .customerPanCardNumber("C123D8790")
                .customerAadharCardNumber("123456789870")
                .customerEmail("anu@gmail.com")
                .customerDateOfBirth("10/03/2000")
                .build();
    }

    @Test
    void createNewCustomer() {
        Customer customer = customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.findByCustomerAadharCardNumberAndCustomerPanCardNumber(customer.getCustomerAadharCardNumber(),
                customer.getCustomerPanCardNumber())).thenReturn(customer);
        assertThrows(CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException.class, () -> {
            customerServices.createNewCustomer(customerDto);
        });
        verify(customerRepo, never()).save(any(Customer.class));
    }

    @Test
    void createNewCustomerWithNegativeScenarioIfCustomerAlreadyExist() {
        Customer customer = customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.findByCustomerAadharCardNumberAndCustomerPanCardNumber(customer.getCustomerAadharCardNumber(),
                customer.getCustomerPanCardNumber())).thenReturn(customer);
        CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException exception = assertThrows(CustomerAlreadyExistWithSameAdharCardAndPanCardNumberException.class, () -> customerServices.createNewCustomer(customerDto));
        assertEquals("Customer is already exist with AdharNumber :123456789870 or with PanCardNumber: C123D8790", exception.getMessage());
    }

    @Test
    void getCustomerById() {
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.existsById(1)).thenReturn(true);
        when(customerRepo.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepo.save(customer)).thenReturn(customer);
        CustomerDto customerDto1 = this.customerConverter.customerModelToDto(customer);
        Optional<CustomerDto> savedUser = customerServices.getCustomerById(customerDto1.getCustomerId());
        assertThat(savedUser).isNotNull();
    }

    @Test
    void getCustomerByIdWithNegativeScenarioIfIdNotExist() {
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.findById(27)).thenReturn(Optional.of(customer));
        assertThrows(CustomerNotFoundException.class, () -> customerServices.getCustomerById(27));
    }

    @Test
    void findAllCustomerDetails() {
        customerServices.findAllCustomerDetails();
        verify(customerRepo).findAll();
    }

    @Test
    void updateCustomerDetails() {
        Integer customerId = 1;
        when(customerRepo.existsById(customerId)).thenReturn(true);
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.save(customer)).thenReturn(customer);
        customerDto.setCustomerEmail("anu@gmail.com");
        customerDto.setCustomerName("Shyam");
        customerDto.setCustomerMobileNumber("9087654321");
        CustomerResponse customerResponse = customerServices.updateCustomerDetails(customerDto, 1);
        assertEquals(CustomerMsgEnum.UPDATE_CUSTOMER.getMessage(), customerResponse.getMessage());

    }

    @Test
    void updateCustomerDetailsWithNegativeScenarioIfIdNotExist() {
        Integer customerId = 189;
        when(customerRepo.existsById(customerId)).thenReturn(false);
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.save(customer)).thenReturn(customer);
        customerDto.setCustomerEmail("anu@gmail.com");
        customerDto.setCustomerName("Shyam");
        customerDto.setCustomerMobileNumber("9087654321");
        CustomerResponse customerResponse = customerServices.updateCustomerDetails(customerDto, 67);
        assertEquals(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage(), customerResponse.getMessage());

    }

    @Test
    void deleteCustomer() {
        Integer customerId = 1;
        when(customerRepo.existsById(customerId)).thenReturn(true);
        CustomerResponse customerResponse = customerServices.deleteCustomer(customerId);
        assertEquals(CustomerMsgEnum.DELETE_CUSTOMER.getMessage(), customerResponse.getMessage());

    }

    @Test
    void deleteCustomerWithNegativeScenarioIfIdNotExist() {
        Integer customerId = 89;
        when(customerRepo.existsById(customerId)).thenReturn(false);
        CustomerResponse customerResponse = customerServices.deleteCustomer(customerId);
        assertEquals(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage(), customerResponse.getMessage());

    }

    @Test
    void getAllAccountsForACustomer() {
        Integer customerId = 1;
        List<Account> accounts = new ArrayList<>();
        accounts.add(Account.builder().accountNumber(1).accountStatus("ACTIVATED").typeOfAccount("Savings").bankName("SBI Bank").totalAmount(0.0).customer(null).build());
        when(customerRepo.existsById(1)).thenReturn(true);
        AllAccountsForACustomerResponse response = customerServices.getAllAccountsForACustomer(customerId);
        assertEquals(CustomerMsgEnum.ALL_ACCOUNTS_FOR_CUSTOMER.getMessage(), response.getMessage());
    }

    @Test
    void getAllAccountsForACustomerWithNegativeScenarioIfIdNotExist() {
        Integer customerId = 67;
        List<Account> accounts = new ArrayList<>();
        accounts.add(Account.builder().accountNumber(1).accountStatus("ACTIVATED").typeOfAccount("Savings").bankName("SBI Bank").totalAmount(0.0).customer(null).build());
        when(customerRepo.existsById(67)).thenReturn(false);
        AllAccountsForACustomerResponse response = customerServices.getAllAccountsForACustomer(customerId);
        assertEquals(CustomerErrors.CUSTOMER_NOT_FOUND.getMessage(), response.getMessage());

    }
}