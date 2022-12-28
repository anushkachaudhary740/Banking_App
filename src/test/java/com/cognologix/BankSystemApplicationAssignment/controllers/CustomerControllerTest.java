package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.responses.CustomerResponse;
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
import static org.mockito.Mockito.when;
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
    private CustomerRepo customerRepo;
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
                .customerId(1)
                .customerName("Anushka Chaudhary")
                .customerGender("female")
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
        mockMvc.perform(post("/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andDo(print())
                .andExpect(jsonPath("$.customerId", is(customerDto.getCustomerId())))
                .andExpect(jsonPath("$.customerName", is(customerDto.getCustomerName())))
                .andReturn();
    }


    @Test
    void getCustomerDetails() throws Exception {
        List<CustomerDto> listOfAccount = new ArrayList<>();
        given(customerServices.findAllCustomerDetails()).willReturn(listOfAccount);
        ResultActions response = mockMvc.perform(get("/customer/get"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAccount.size())));
    }
    @Test
    void getCustomerDetailsWithNegativeScenarioIfCustomerDetailsNotFound() throws Exception {
        List<CustomerDto> listOfAccount = new ArrayList<>();
        given(customerServices.findAllCustomerDetails()).willReturn(List.of());
        ResultActions response = mockMvc.perform(get("/customer/get"));
        response.andExpect(status().isNotFound())
                .andReturn();

    }
    @Test
    void findCustomerDetailsById() throws Exception {
        Integer customerId = 501;
        given(customerServices.getCustomerById(customerId)).willReturn(Optional.ofNullable(customerDto));
        ResultActions response = mockMvc.perform(get("/customer/get/{customerId}", customerId));
        response
                .andDo(print())
                .andExpect(jsonPath("$.customerId", is(customerDto.getCustomerId())))
                .andExpect(jsonPath("$.customerName", is(customerDto.getCustomerName())))
                .andExpect(jsonPath("$.customerEmail", is(customerDto.getCustomerEmail())));
    }
    @Test
    void findCustomerDetailsByIdWithNegativeScenarioIfIdNotExist() throws Exception {
        Integer customerId = 51;
        given(customerServices.getCustomerById(customerId)).willReturn(Optional.empty());
        ResultActions response = mockMvc.perform(get("/customer/get/{customerId}", customerId));
        response.andExpect(status().isNotFound())
                .andReturn();
                 }
    @Test
    void updateCustomerDetails() throws Exception {
        Integer customerId=1;
        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .customerName("Anu Chaudhary")
                .customerGender("female")
                .customerMobileNumber("7408336672")
                .customerPanCardNumber("C123D8790")
                .customerAadharCardNumber("1234 5678 9870")
                .customerEmail("anushka@gmail.com")
                .customerDateOfBirth("2000/20000/2000")
                .build();
        CustomerResponse customerResponse=new CustomerResponse("Customer details updated successfully",true,customerDto);
        when(customerServices.updateCustomerDetails(updatedCustomerDto,customerId)).thenReturn(customerResponse);
        ResultActions response = mockMvc.perform(put("/customer/update/{customerId}",customerId)
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(customerResponse)));
        response.andExpect(status().isOk())
                .andDo(print());
}
    @Test
    void updateCustomerDetailsWithNegativeScenarioIfIdNotExist() throws Exception {
        Integer customerId=78;
        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .customerName("Anu Chaudhary")
                .customerGender("female")
                .customerMobileNumber("7408336672")
                .customerPanCardNumber("C123D8790")
                .customerAadharCardNumber("1234 5678 9870")
                .customerEmail("anushka@gmail.com")
                .customerDateOfBirth("2000/20000/2000")
                .build();
        CustomerResponse customerResponse=new CustomerResponse("Customer Id dose not exist",false,null);
        when(customerServices.updateCustomerDetails(updatedCustomerDto,customerId)).thenReturn(customerResponse);
        ResultActions response = mockMvc.perform(put("/customer/update/{customerId}",customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerResponse)));
        response.andDo(print())
                .andReturn();
                //.andExpect(jsonPath("$.message",is(customerResponse.getMessage())));
    }
    @Test
    void deleteCustomerDetailsById() throws Exception {
        CustomerResponse customerResponse=new CustomerResponse();
        customerResponse.setMessage("Customer details deleted successfully..");
        customerResponse.setSuccess(true);
        customerResponse.setCustomer(null);
        Integer customerId= 101;
        when(customerServices.deleteCustomer(customerId)).thenReturn(customerResponse);
        ResultActions response = mockMvc.perform(delete("/customer/delete/{customerId}",customerId));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message",is(customerResponse.getMessage())));
    }
    @Test
    void deleteCustomerDetailsByIdWithNegativeScenarioIfIdNotExist() throws Exception {
        CustomerResponse customerResponse=new CustomerResponse();
        customerResponse.setMessage("Customer Id does not exist");
        customerResponse.setSuccess(false);
        customerResponse.setCustomer(null);
        Integer customerId= 10;
        when(customerServices.deleteCustomer(customerId)).thenReturn(customerResponse);
        ResultActions response = mockMvc.perform(delete("/customer/delete/{customerId}",customerId));
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message",is(customerResponse.getMessage())));

    }
}