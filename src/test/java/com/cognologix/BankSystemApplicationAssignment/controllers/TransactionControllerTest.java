package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.AccountErrors;
import com.cognologix.BankSystemApplicationAssignment.enums.transactionEnums.TransactionEnum;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import com.cognologix.BankSystemApplicationAssignment.responses.AllTransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
class TransactionControllerTest {
    @MockBean
    private TransactionServices transactionServices;
    //use to call REST APIs
    @Autowired
    private MockMvc mockMvc;
    //used to convert java object into JSON
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountRepo accountRepo;
    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        transactionDto = TransactionDto.builder()
                .toAccountNumber(1)
                .fromAccountNumber(2)
                .transferAmount(0.00)
                .status("amount deposited")
                .build();
    }

    @Test
    void getAllTransactionDetailsForOneAccount() throws Exception {
        Integer accountNumber = 1;
        List<Transaction> listOfTransaction = new ArrayList<>();
        listOfTransaction.add(Transaction.builder().toAccountNumber(1).fromAccountNumber(0).transferAmount(567.00).build());
        AllTransactionsResponse transactionsResponse = new AllTransactionsResponse(TransactionEnum.ALL_TRANSACTIONS.getMessage(), true, listOfTransaction);
        log.info(transactionsResponse.getMessage());
        when(transactionServices.getAllTransactionDetailsForOneAccount(accountNumber)).thenReturn(transactionsResponse);
        ResultActions response = mockMvc.perform(get("/transaction/account/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getTransactionDetailsWithNegativeScenarioIfAccountNotExist() throws Exception {
        Integer accountNumber = 89;
        List<Transaction> listOfTransaction = new ArrayList<>();
        AllTransactionsResponse transactionsResponse = new AllTransactionsResponse(TransactionEnum.ALL_TRANSACTIONS.getMessage(), false, listOfTransaction);
        log.error(transactionsResponse.getMessage());
        when(transactionServices.getAllTransactionDetailsForOneAccount(accountNumber)).thenReturn(transactionsResponse);
        ResultActions response = mockMvc.perform(get("/transaction/account/{accountNumber}", accountNumber));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void findTransactionDetailsById() throws Exception {
        Integer transactionId = 12;
        when(transactionServices.getTransactionDetailsById(transactionId)).thenReturn(Optional.ofNullable(transactionDto));
        ResultActions response = mockMvc.perform(get("/transaction/get/{transactionId}", transactionId));
        response
                .andDo(print())
                .andExpect(jsonPath("$.toAccountNumber", is(transactionDto.getToAccountNumber())))
                .andExpect(jsonPath("$.fromAccountNumber", is(transactionDto.getFromAccountNumber())))
                .andExpect(jsonPath("$.transferAmount", is(transactionDto.getTransferAmount())));
    }

    @Test
    void findTransactionDetailsByIdWithNegativeScenarioIfIdNotExist() throws Exception {
        Integer transactionId = 67;
        when(transactionServices.getTransactionDetailsById(transactionId)).thenReturn(Optional.empty());
        ResultActions response = mockMvc.perform(get("/transaction/get/{transactionId}", transactionId));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void depositAmount() throws Exception {
        TransactionsResponse transactionsResponse = new TransactionsResponse(TransactionEnum.DEPOSIT_AMOUNT.getMessage(), true, transactionDto);
        log.info(transactionsResponse.getMessage());
        when(transactionServices.depositAmount(1, 500.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/deposit?accountNumber=1&depositAmount=500")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message", is(TransactionEnum.DEPOSIT_AMOUNT.getMessage())))
                .andExpect(jsonPath("$.success", is(true)))
                .andReturn();
    }

    @Test
    void depositAmountWithNegativeScenarioIfAccountNotExist() throws Exception {
        TransactionsResponse transactionsResponse = new TransactionsResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), false, null);
        log.error(transactionsResponse.getMessage());
        when(transactionServices.depositAmount(67, 500.0)).thenReturn(transactionsResponse);
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
        TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setMessage(TransactionEnum.WITHDRAW_AMOUNT.getMessage());
        transactionsResponse.setSuccess(true);
        transactionsResponse.setTransaction(null);
        when(transactionServices.withdrawAmount(1, 20.0)).thenReturn(transactionsResponse);
        log.info(transactionsResponse.getMessage());
        ResultActions result = mockMvc.perform(
                put("/transaction/withdraw?accountNumber=1&withdrawAmount=20")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message", is(TransactionEnum.WITHDRAW_AMOUNT.getMessage())))
                .andExpect(jsonPath("$.success", is(true)))
                .andReturn();
    }

    @Test
    void withdrawAmountWithNegativeScenarioIfAccountNotExist() throws Exception {
        TransactionsResponse transactionsResponse = new TransactionsResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), false, null);
        log.error(transactionsResponse.getMessage());
        when(transactionServices.withdrawAmount(90, 20.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/withdraw?accountNumber=90&withdrawAmount=20")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isNotFound()).andExpect(jsonPath("$.message", is(AccountErrors.ACCOUNT_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.success", is(false)))
                .andReturn();
    }

    @Test
    void moneyTransfer() throws Exception {
        TransactionsResponse transactionsResponse = new TransactionsResponse(TransactionEnum.TRANSFER.getMessage(), true, transactionDto);
        log.info(transactionsResponse.getMessage());
        when(transactionServices.transferAmount(2, 1, 40.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/amount/transfer?senderAccountNumber=2&receiverAccountNumber=1&amount=40")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isOk()).andExpect(jsonPath("$.message", is(TransactionEnum.TRANSFER.getMessage())))
                .andExpect(jsonPath("$.success", is(true)))
                .andReturn();
    }

    @Test
    void moneyTransferWithNegativeScenarioIfAccountNotExist() throws Exception {
        TransactionsResponse transactionsResponse = new TransactionsResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), false, null);
        log.error(transactionsResponse.getMessage());
        when(transactionServices.transferAmount(67, 87, 40.0)).thenReturn(transactionsResponse);
        ResultActions result = mockMvc.perform(
                put("/transaction/amount/transfer?senderAccountNumber=67&receiverAccountNumber=87&amount=40")
                        .content(objectMapper.writeValueAsString(transactionsResponse)).contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status()
                        .isNotFound()).andExpect(jsonPath("$.message", is(AccountErrors.ACCOUNT_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.success", is(false)))
                .andReturn();
    }

}