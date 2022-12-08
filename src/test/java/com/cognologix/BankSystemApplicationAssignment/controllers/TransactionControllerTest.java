package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ApiResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {
    @MockBean
    private TransactionServices transactionServices;
    //use to call REST APIs
    @Autowired
    private MockMvc mockMvc;
    //used to convert object into JSON
    @Autowired
    private ObjectMapper objectMapper;

    private TransactionDto transactionDto;

    @Test
    void getTransactionDetails() {

    }
@BeforeEach
void setUp(){
    transactionDto = TransactionDto.builder()
            .transactionId(21)
            .toAccountNumber(1)
            .fromAccountNumber(2)
            .transferAmount(0.00)
            .status("Transaction testing")
            .build();
}
    @Test
    void testGetTransactionDetails() throws Exception {
        List<TransactionDto> listOfAccount = new ArrayList<>();
        //given - setup
        given(transactionServices.getTransactionDetails()).willReturn(listOfAccount);
        // when -  the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/transaction/get"));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAccount.size())));
    }
    @Test
    void findTransactionDetailsById() throws Exception {
        Integer transactionId = 21;
        given(transactionServices.getTransactionDetailsById(transactionId)).willReturn(transactionDto);
        ResultActions response = mockMvc.perform(get("/transaction/get/{transactionId}", transactionId));
        response
                .andDo(print())
                .andExpect(jsonPath("$.transactionId", is(transactionDto.getTransactionId())))
                .andExpect(jsonPath("$.toAccountNumber", is(transactionDto.getToAccountNumber())))
                .andExpect(jsonPath("$.fromAccountNumber", is(transactionDto.getFromAccountNumber())))
                .andExpect(jsonPath("$.transferAmount", is(transactionDto.getTransferAmount())));
    }

    @Test
    void depositAmount() throws Exception {
        String content = objectMapper.writeValueAsString(transactionDto);
        ResultActions result = mockMvc.perform(
                put("/transaction/deposit?accountNumber=2&depositAmount=500",transactionDto.getToAccountNumber(),500.00)
                        .content(content).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message",is("Rs 500.0 Successfully deposit.....")))
                        .andExpect(jsonPath("$.success",is(true)))
                        .andReturn();
    }

    @Test
    void withdrawAmount() throws Exception {
        String content = objectMapper.writeValueAsString(transactionDto);
        ResultActions result = mockMvc.perform(
                put("/transaction/withdraw?accountNumber=1&withdrawAmount=300",transactionDto.getToAccountNumber(),300.00)
                        .content(content).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message",is("Rs 300.0 Successfully withdraw......")))
                .andExpect(jsonPath("$.success",is(true)))
                .andReturn();
    }

    @Test
    void moneyTransfer() throws Exception {
        String content = objectMapper.writeValueAsString(transactionDto);
        ResultActions result = mockMvc.perform(
                put("/transaction/amount/transfer?senderAccountNumber=2&receiverAccountNumber=1&amount=100",transactionDto.getFromAccountNumber(),transactionDto.getToAccountNumber(),100.00)
                        .content(content).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message",is("Rs 100.0 successfully transfer......")))
                .andExpect(jsonPath("$.success",is(true)))
                .andReturn();
    }


}