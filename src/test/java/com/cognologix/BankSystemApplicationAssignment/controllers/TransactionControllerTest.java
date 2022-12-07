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
            .transferAmount(200.00)
            .build();
}
    @Test
    void findTransactionDetailsById() throws Exception {
        // given - precondition or setup
        Integer transactionId = 21;
        given(transactionServices.getTransactionDetailsById(transactionId)).willReturn(transactionDto);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/transaction/get/{transactionId}", transactionId));
        // then - verify the output
        response
                .andDo(print())
                .andExpect(jsonPath("$.transactionId", is(transactionDto.getTransactionId())))
                .andExpect(jsonPath("$.toAccountNumber", is(transactionDto.getToAccountNumber())))
                .andExpect(jsonPath("$.fromAccountNumber", is(transactionDto.getFromAccountNumber())))
                .andExpect(jsonPath("$.transferAmount", is(transactionDto.getTransferAmount())));
    }

    @Test
    void depositAmount() throws Exception {
        TransactionDto accMoneyTransferSaveDto = new TransactionDto();
        accMoneyTransferSaveDto.setFromAccountNumber(1);
        accMoneyTransferSaveDto.setToAccountNumber(2);
        accMoneyTransferSaveDto.setTransferAmount(100.00);
        accMoneyTransferSaveDto.setStatus("amount transfer testing");

        String content = objectMapper.writeValueAsString(accMoneyTransferSaveDto);

        MvcResult result = mockMvc.perform(
                put("transaction/amount/transfer",accMoneyTransferSaveDto.getFromAccountNumber(),accMoneyTransferSaveDto.getToAccountNumber(),accMoneyTransferSaveDto.getTransferAmount()).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        MvcResult isSuccess = result;

        assertTrue((BooleanSupplier) isSuccess);

    }

    @Test
    void withdrawAmount() {
    }

    @Test
    void moneyTransfer() {
    }

}