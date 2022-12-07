package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.CustomerServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {
    @MockBean
    private CustomerServices customerServices;
//use to call REST APIs
    @Autowired
    private MockMvc mockMvc;
//used to convert object into JSON
    @Autowired
    private ObjectMapper objectMapper;
    private CustomerDto customerDto;
    @BeforeEach
    void setUp(){
        customerDto = CustomerDto.builder()
                .customerId(101)
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
    void createCustomer() throws Exception {
        given(customerServices.createNewCustomer(any(CustomerDto.class)))
                .willAnswer((e)-> e.getArgument(0));
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)));
        //then - verify
        response.andDo(print())
                .andExpect(jsonPath("$.customerId", is(customerDto.getCustomerId())))
                .andExpect(jsonPath("$.customerName", is(customerDto.getCustomerName())))
                .andExpect(jsonPath("$.gender", is(customerDto.getGender())));
    }

    @Test
    void getCustomerDetails() throws Exception {
        List<CustomerDto> listOfAccount = new ArrayList<>();
        listOfAccount.add(CustomerDto.builder().customerId(56).customerName("Ram").gender("male").customerEmail("ram@gmail.com").customerMobileNumber("786543890").customerAadharCardNumber("1238 4567 8907").customerPanCardNumber("1C234D56").customerDateOfBirth("00/00/00").build());
        listOfAccount.add(CustomerDto.builder().customerId(89).customerName("Mohan").gender("male").customerEmail("mohan@gmail.com").customerMobileNumber("7408336672").customerAadharCardNumber("5432 1234 8765").customerPanCardNumber("546C7678D").customerDateOfBirth("00/00/00").build());
        given(customerServices.findAllCustomerDetails()).willReturn(listOfAccount);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/customer/get"));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAccount.size())));
    }
    @Test
    void findCustomerDetailsById() throws Exception {
        // given - precondition or setup
        Integer customerId = 501;
        given(customerServices.getCustomerById(customerId)).willReturn(Optional.ofNullable(customerDto));
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/customer/get/{customerId}", customerId));
        // then - verify the output
        response
                .andDo(print())
                .andExpect(jsonPath("$.customerId", is(customerDto.getCustomerId())))
                .andExpect(jsonPath("$.customerName", is(customerDto.getCustomerName())))
                .andExpect(jsonPath("$.customerEmail", is(customerDto.getCustomerEmail())));
    }

    @Test
    void updateAccountDetails() throws Exception {
        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .customerName("Anu Chaudhary")
                .gender("female")
                .customerMobileNumber("7408336672")
                .customerPanCardNumber("C123D8790")
                .customerAadharCardNumber("1234 5678 9870")
                .customerEmail("anushka@gmail.com")
                .customerDateOfBirth("2000/20000/2000")
                .build();
        given(customerServices.getCustomerById(customerDto.getCustomerId())).willReturn(Optional.ofNullable(customerDto));
        given(customerServices.updateCustomerDetails(any(CustomerDto.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/customer/update/{customerId}",customerDto.getCustomerId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomerDto)));
        //then - verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.customerName", is(updatedCustomerDto.getCustomerName())))
                .andExpect(jsonPath("$.customerEmail", is(updatedCustomerDto.getCustomerEmail())));
    }

    @Test
    void deleteCustomerDetailsById() throws Exception {
        // given - precondition or setup
        Integer customerId= 101;
        willDoNothing().given(customerServices).deleteCustomer(customerId);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/customer/delete/{customerId}",customerId));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}