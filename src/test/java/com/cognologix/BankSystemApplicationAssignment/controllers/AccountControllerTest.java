package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.AccountServices;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountServices accountServices;
    private AccountDto accountDto;
    @Test
    void saveAccount() throws Exception {
        // given - precondition or setup
         AccountDto accountDto= AccountDto.builder()
                .bankName("sbi bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        given(accountServices.createAccount(accountDto))
        .willReturn(accountDto);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/account/create")
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)));
        //then - verify
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.bankName", is(accountDto.getBankName())))
                .andExpect(jsonPath("$.typeOfAccount", is(accountDto.getTypeOfAccount())))
                .andExpect(jsonPath("$.totalAmount", is(accountDto.getTotalAmount())));
    }

    @Test
    void getAccountDetails() throws Exception {
        List<AccountDto> listOfAccount = new ArrayList<>();
        listOfAccount.add(AccountDto.builder().accountNumber(12).bankName("HDFC Bank").typeOfAccount("saving").totalAmount(567.00).build());
        listOfAccount.add(AccountDto.builder().accountNumber(23).bankName("SBI Bank").typeOfAccount("current").totalAmount(600.00).build());
        given(accountServices.getAccountDetails()).willReturn(listOfAccount);
        ResultActions response = mockMvc.perform(get("/account/get"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAccount.size())));

    }

    @Test
    void findAccountDetailsById() throws Exception {
        Integer accountNumber = 1;
        AccountDto accountDto= AccountDto.builder()
                .bankName("sbi bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        given(accountServices.getAccountDetailsByNumber(accountNumber)).willReturn(Optional.ofNullable(accountDto));
        ResultActions response = mockMvc.perform(get("/account/get/{id}", accountNumber));
        response
                .andDo(print())
                .andExpect(jsonPath("$.bankName", is(accountDto.getBankName())))
                .andExpect(jsonPath("$.typeOfAccount", is(accountDto.getTypeOfAccount())))
                .andExpect(jsonPath("$.totalAmount", is(accountDto.getTotalAmount())));

    }

    @Test
    void updateAccountDetails() throws Exception {
        // given - precondition or setup
        Integer accountNumber = 1;
        AccountDto accountDto= AccountDto.builder()
                .bankName("sbi bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        AccountDto updatedAccountDto= AccountDto.builder()
                .bankName("HDFC bank")
                .typeOfAccount("current")
                .totalAmount(600.00)
                .build();
        given(accountServices.getAccountDetailsByNumber(accountNumber)).willReturn(Optional.of(accountDto));
        given(accountServices.updateAccount(any(AccountDto.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));
        ResultActions response = mockMvc.perform(put("/account/update/{accountNumber}",accountNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAccountDto)));
        response
                .andDo(print())
                .andExpect(jsonPath("$.bankName", is(updatedAccountDto.getBankName())))
                .andExpect(jsonPath("$.typeOfAccount", is(updatedAccountDto.getTypeOfAccount())))
                .andExpect(jsonPath("$.totalAmount", is(updatedAccountDto.getTotalAmount())));
    }

    @Test
    void deleteAccountDetailsById() throws Exception {
        AccountResponse accountResponse=new AccountResponse();
        accountResponse.setMessage("Account deleted successfully..");
        accountResponse.setSuccess(true);
        Integer accountNumber= 12;
        when(accountServices.deleteAccount(12)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message",is(accountResponse.getMessage())))
                .andReturn();

    }

    @Test
    void findTotalBalance() throws Exception {
        AccountResponse accountResponse=new AccountResponse();
        accountResponse.setMessage("Total balance : 6786.0");
        accountResponse.setSuccess(true);
        Integer accountNumber=1;
        when(accountServices.getTotalBalance(1)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(get("/account/amount/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message",is(accountResponse.getMessage())))
                .andReturn();
    }
}