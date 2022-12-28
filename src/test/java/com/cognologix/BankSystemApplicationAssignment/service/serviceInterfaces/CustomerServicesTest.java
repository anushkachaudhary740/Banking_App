package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.AccountAlreadyExistException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.CustomerAlreadyExistException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.CustomerNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.CustomerResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.IntegerConversion;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
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

    @BeforeEach
    void setUp(){
        customerDto = CustomerDto.builder()
                .customerId(1)
                .customerName("Anu Chaudhary")
                .customerGender("female")
                .customerMobileNumber("1234567890")
                .customerPanCardNumber("C123D8790")
                .customerAadharCardNumber("1234 5678 9870")
                .customerEmail("anu@gmail.com")
                .customerDateOfBirth("10/03/2000")
                .build();
    }

    @Test
    void createNewCustomer() {
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.findById(customer.getCustomerId()))
                .thenReturn(Optional.of(customer));
        when(customerRepo.save(customer)).thenReturn(customer);
        CustomerDto savedCustomer = customerServices.createNewCustomer(customerDto);
        assertThat(savedCustomer).isNotNull();

    }
    @Test
    void createNewCustomerWithNegativeScenarioIfCustomerAlreadyExist() {
        Customer customer=customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.existsById(1)).thenReturn(true);
        when(customerRepo.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepo.save(customer)).thenReturn(customer);
        assertThrows(CustomerAlreadyExistException.class, () ->
                customerServices.createNewCustomer(customerDto)
        );
    }

    @Test
    void getCustomerById() {
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.existsById(1)).thenReturn(true);
        when(customerRepo.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepo.save(customer)).thenReturn(customer);
        CustomerDto customerDto1=this.customerConverter.customerModelToDto(customer);
        Optional<CustomerDto> savedUser = customerServices.getCustomerById(customerDto1.getCustomerId());
       assertThat(savedUser).isNotNull();
}
@Test
void getCustomerByIdWithNegativeScenarioIfIdNotExist(){
    Customer customer = this.customerConverter.customerDtoToModel(customerDto);
    when(customerRepo.findById(27)).thenReturn(Optional.of(customer));
    assertThrows(CustomerNotFoundException.class,()->customerServices.getCustomerById(27));
}

    @Test
    void findAllCustomerDetails() {
        customerServices.findAllCustomerDetails();
        verify(customerRepo).findAll();
    }
    @Test
   void updateCustomerDetails() {
        Integer customerId=1;
        when(customerRepo.existsById(customerId)).thenReturn(true);
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.save(customer)).thenReturn(customer);
        customerDto.setCustomerEmail("anu@gmail.com");
        customerDto.setCustomerName("Shyam");
        customerDto.setCustomerMobileNumber("9087654321");
        CustomerResponse customerResponse = customerServices.updateCustomerDetails(customerDto,1);
        assertEquals("Customer details updated successfully",customerResponse.getMessage());

    }
    @Test
    void updateCustomerDetailsWithNegativeScenarioIfIdNotExist() {
        Integer customerId=189;
        when(customerRepo.existsById(customerId)).thenReturn(false);
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        when(customerRepo.save(customer)).thenReturn(customer);
        customerDto.setCustomerEmail("anu@gmail.com");
        customerDto.setCustomerName("Shyam");
        customerDto.setCustomerMobileNumber("9087654321");
        CustomerResponse customerResponse = customerServices.updateCustomerDetails(customerDto,67);
        assertEquals("Customer Id does not exist",customerResponse.getMessage());

    }

    @Test
    void deleteCustomer() {
        Integer customerId = 1;
        when(customerRepo.existsById(customerId)).thenReturn(true);
        CustomerResponse customerResponse=customerServices.deleteCustomer(customerId);
        assertEquals("Customer details deleted successfully..",customerResponse.getMessage());

    }
    @Test
    void deleteCustomerWithNegativeScenarioIfIdNotExist() {
        Integer customerId = 89;
        when(customerRepo.existsById(customerId)).thenReturn(false);
        CustomerResponse customerResponse=customerServices.deleteCustomer(customerId);
        assertEquals("Customer Id does not exist",customerResponse.getMessage());

    }

}