package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dto.AmountTransferDto;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

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

    private AmountTransferDto amountTransferDto;
    private TransactionDto transactionDto;
    @Test
    void getTransactionDetails() {

    }
@BeforeEach
void setUp(){
    amountTransferDto = AmountTransferDto.builder()
            .toAccountNumber(1)
            .fromAccountNumber(2)
            .transferAmount(0.00)
            .status("amount deposited")
            .build();
     transactionDto = TransactionDto.builder()
            .toAccountNumber(1)
            .transferAmount(500.0)
            .status("amount deposited")
            .build();
}
    @Test
    void testGetTransactionDetails() throws Exception {
        List<AmountTransferDto> listOfAccount = new ArrayList<>();
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
        given(transactionServices.getTransactionDetailsById(transactionId)).willReturn(amountTransferDto);
        ResultActions response = mockMvc.perform(get("/transaction/get/{transactionId}", transactionId));
        response
                .andDo(print())
                .andExpect(jsonPath("$.toAccountNumber", is(amountTransferDto.getToAccountNumber())))
                .andExpect(jsonPath("$.fromAccountNumber", is(amountTransferDto.getFromAccountNumber())))
                .andExpect(jsonPath("$.transferAmount", is(amountTransferDto.getTransferAmount())));
    }

    @Test
    void depositAmount() throws Exception {
        TransactionDto transactionDto = TransactionDto.builder()
                .toAccountNumber(1)
                .transferAmount(500.0)
                .status("amount deposited")
                .build();
        String content = objectMapper.writeValueAsString(transactionDto);
        ResultActions result = mockMvc.perform(
                put("/transaction/deposit?accountNumber=1&depositAmount=500")
                        .content(content).contentType(MediaType.APPLICATION_JSON)
        );
        //System.out.println(transactionDto.getStatus());
        result.andExpect(status()
//                        .isOk()).andExpect(jsonPath("$.message",is("Rs 500.0 Successfully deposit.....")))
//                        .andExpect(jsonPath("$.success",is(true)))
//                        .andReturn();
                .isOk()).andExpect(jsonPath("$.status",is("amount deposited")))
                .andReturn();
    }

    @Test
    void withdrawAmount() throws Exception {
        TransactionDto amountTransferDto = TransactionDto.builder()
                .toAccountNumber(1)
                .transferAmount(300.0)
                .status("amount withdrew")
                .build();
        String content = objectMapper.writeValueAsString(amountTransferDto);
        ResultActions result = mockMvc.perform(
                put("/transaction/withdraw?accountNumber=1&withdrawAmount=300", amountTransferDto.getToAccountNumber(),300.00)
                        .content(content).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.transferAmount",is(amountTransferDto.getTransferAmount())))
                //.andExpect(jsonPath("$.status",is("amount withdrew")))
                .andReturn();
//        mockMvc.perform(MockMvcRequestBuilders.put("/transaction/withdraw?accountNumber=1&withdrawAmount=300"))
//                .andExpect(status().isOk())
                //result.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                result.andExpect(jsonPath("$.transferAmount",is(amountTransferDto.getTransferAmount())))
//                .andExpect(jsonPath("$.status",is("amount withdrew")))
//                .andReturn();
    }

    @Test
    void moneyTransfer() throws Exception {
        amountTransferDto = AmountTransferDto.builder()
                .toAccountNumber(1)
                .fromAccountNumber(2)
                .transferAmount(40.0)
                .status("40.0 Rs withdrew from account 2 and deposited in account 1")
                .build();
        String content = objectMapper.writeValueAsString(amountTransferDto);
        ResultActions result = mockMvc.perform(
                put("/transaction/amount/transfer?senderAccountNumber=2&receiverAccountNumber=1&amount=40",amountTransferDto.getFromAccountNumber(),amountTransferDto.getToAccountNumber(),40.0)
                        .content(content).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.transferAmount",is(amountTransferDto.getTransferAmount())))
                .andExpect(jsonPath("$.status",is("40.0 Rs withdrew from account 2 and deposited in account 1")))
                .andReturn();
    }


}