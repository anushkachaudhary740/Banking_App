package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @MockBean
    private TransactionServices transactionServices;
    //use to call REST APIs
    @Autowired
    private MockMvc mockMvc;
    //used to convert object into JSON
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountRepo accountRepo;
    private TransactionDto transactionDto1;

@BeforeEach
void setUp(){
    transactionDto1 = TransactionDto.builder()
            .transactionId(21)
            .toAccountNumber(1)
            .fromAccountNumber(2)
            .transferAmount(0.00)
            .status("amount deposited")
            .build();
}
    @Test
    void getTransactionDetails() throws Exception {
        List<TransactionDto> listOfTransaction = new ArrayList<>();
        listOfTransaction.add(TransactionDto.builder().transactionId(12).toAccountNumber(1).fromAccountNumber(0).transferAmount(567.00).build());
        when(transactionServices.getTransactionDetails()).thenReturn(listOfTransaction);
        ResultActions response = mockMvc.perform(get("/transaction/get"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfTransaction.size())));
    }
    @Test
    void getTransactionDetailsWithNegativeScenarioIfDetailsNotFound() throws Exception {
        List<TransactionDto> listOfTransaction = new ArrayList<>();
        when(transactionServices.getTransactionDetails()).thenReturn(List.of());
        ResultActions response = mockMvc.perform(get("/transaction/get"));
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfTransaction.size())));
    }
    @Test
    void findTransactionDetailsById() throws Exception {
        when(transactionServices.getTransactionDetailsById(transactionDto1.getTransactionId())).thenReturn(Optional.ofNullable(transactionDto1));
        ResultActions response = mockMvc.perform(get("/transaction/get/{transactionId}", transactionDto1.getTransactionId()));
        response
                .andDo(print())
                .andExpect(jsonPath("$.toAccountNumber", is(transactionDto1.getToAccountNumber())))
                .andExpect(jsonPath("$.fromAccountNumber", is(transactionDto1.getFromAccountNumber())))
                .andExpect(jsonPath("$.transferAmount", is(transactionDto1.getTransferAmount())));
    }
    @Test
    void findTransactionDetailsByIdWithNegativeScenarioIfIdNotExist() throws Exception {
    Integer transactionId=67;
        when(transactionServices.getTransactionDetailsById(transactionDto1.getTransactionId())).thenReturn(Optional.empty());
        ResultActions response = mockMvc.perform(get("/transaction/get/{transactionId}", transactionDto1.getTransactionId()));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void depositAmount() throws Exception {
        TransactionsResponse transactionsResponse =new TransactionsResponse("Rs 500.0 Successfully deposit.....",true,transactionDto1);
        when(transactionServices.depositAmount(1,500.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/deposit?accountNumber=1&depositAmount=500")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message",is("Rs 500.0 Successfully deposit.....")))
                        .andExpect(jsonPath("$.success",is(true)))
                        .andReturn();
    }
    @Test
    void depositAmountWithNegativeScenarioIfAccountNotExist() throws Exception {
        TransactionsResponse transactionsResponse =new TransactionsResponse("Account does not exist",false,null);
        when(transactionServices.depositAmount(67,500.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/deposit?accountNumber=67&depositAmount=500")
                        .content(objectMapper.writeValueAsString(transactionsResponse))
                        .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status()
                        .isNotFound())
                .andReturn();
    }

    @Test
    void withdrawAmount() throws Exception {
        TransactionsResponse transactionsResponse =new TransactionsResponse();
        transactionsResponse.setMessage("Rs 20.0 successfully withdraw.....");
        transactionsResponse.setSuccess(true);
        transactionsResponse.setTransaction(null);
        when(transactionServices.withdrawAmount(1,20.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/withdraw?accountNumber=1&withdrawAmount=20")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message",is("Rs 20.0 successfully withdraw.....")))
                .andExpect(jsonPath("$.success",is(true)))
                .andReturn();
    }
    @Test
    void withdrawAmountWithNegativeScenarioIfAccountNotExist() throws Exception {
        TransactionsResponse transactionsResponse =new TransactionsResponse("Account does not exist",false,null);
        when(transactionServices.withdrawAmount(90,20.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/withdraw?accountNumber=90&withdrawAmount=20")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isNotFound()).andExpect(jsonPath("$.message",is("Account does not exist")))
                .andExpect(jsonPath("$.success",is(false)))
                .andReturn();
    }

    @Test
    void moneyTransfer() throws Exception {
        TransactionsResponse transactionsResponse =new TransactionsResponse("Rs 40.0 successfully transfer.....",true,transactionDto1);
        when(transactionServices.transferAmount(2,1,40.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/amount/transfer?senderAccountNumber=2&receiverAccountNumber=1&amount=40")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message",is("Rs 40.0 successfully transfer.....")))
                .andExpect(jsonPath("$.success",is(true)))
                .andReturn();
        }
        @Test
    void moneyTransferWithNegativeScenarioIfAccountNotExist() throws Exception {
            TransactionsResponse transactionsResponse =new TransactionsResponse("Account does not exist",false,null);
            when(transactionServices.transferAmount(67,87,40.0)).thenReturn(transactionsResponse);
            ResultActions result = mockMvc.perform(
                    put("/transaction/amount/transfer?senderAccountNumber=67&receiverAccountNumber=87&amount=40")
                            .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
            );
            result.andExpect(status()
                            .isNotFound()).andExpect(jsonPath("$.message",is("Account does not exist")))
                    .andExpect(jsonPath("$.success",is(false)))
                    .andReturn();
        }

}