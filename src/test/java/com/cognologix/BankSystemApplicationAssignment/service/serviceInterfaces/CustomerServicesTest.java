package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
                .customerId(199)
                .customerName("Anushka Chaudhary")
                .gender("female")
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
        given(customerRepo.findById(customer.getCustomerId()))
                .willReturn(Optional.of(customer));
        given(customerRepo.save(customer)).willReturn(customer);
        CustomerDto savedCustomer = customerServices.createNewCustomer(customerDto);
        System.out.println("Saved Customer is "+savedCustomer);
        assertThat(savedCustomer).isNotNull();

    }

    @Test
    void getCustomerById() {
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);

        //given
        given(customerRepo.findById(199)).willReturn(Optional.of(customer));
        // when
        Optional<CustomerDto> savedUser = customerServices.getCustomerById(customerDto.getCustomerId());
        // then
       assertThat(savedUser).isNotNull();
}

    @Test
    void findAllCustomerDetails() {
        customerServices.findAllCustomerDetails();
        verify(customerRepo).findAll();
    }
    @Test
    void updateCustomerDetails() {
        // given -setup
        Customer customer = this.customerConverter.customerDtoToModel(customerDto);
        given(customerRepo.save(customer)).willReturn(customer);
        customerDto.setCustomerName("Anu");
        customerDto.setCustomerEmail("anu@gmail.com");
        customerDto.setCustomerMobileNumber("8765986545");
        customerDto.setCustomerAadharCardNumber("9087 7654 3456");
        customerDto.setCustomerDateOfBirth("10/00/10");
        customerDto.setCustomerPanCardNumber("12C3456D789");
        // when -  action or the behaviour that we are going test
        CustomerDto updatedCustomer = customerServices.updateCustomerDetails(customerDto);
        // then - verify the output
        Assertions.assertThat(updatedCustomer.getCustomerName()).isEqualTo("Anu");
        Assertions.assertThat(updatedCustomer.getCustomerPanCardNumber()).isEqualTo("12C3456D789");
        Assertions.assertThat(updatedCustomer.getCustomerMobileNumber()).isEqualTo("8765986545");
    }

    @Test
    void deleteCustomer() {
        Integer customerId = 200;
        willDoNothing().given(customerRepo).deleteById(customerId);
        customerServices.deleteCustomer(customerId);
        // then - verify the output
        verify(customerRepo, times(1)).deleteById(customerId);

    }
}