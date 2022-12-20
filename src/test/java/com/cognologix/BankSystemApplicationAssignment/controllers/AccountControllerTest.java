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
import static org.mockito.BDDMockito.given;
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
    void createAccount() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(28)
                .bankName("sbi bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        when(accountServices.createAccount(accountDto)).thenReturn(accountDto);

        ResultActions response = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)));

        response.andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void getAccountDetails() throws Exception {
        List<AccountDto> listOfAccount = new ArrayList<>();
        listOfAccount.add(AccountDto.builder().accountNumber(12).bankName("HDFC Bank").typeOfAccount("saving").totalAmount(567.00).build());
        listOfAccount.add(AccountDto.builder().accountNumber(23).bankName("SBI Bank").typeOfAccount("current").totalAmount(600.00).build());
        given(accountServices.getAccountDetails()).willReturn(listOfAccount);
        ResultActions response = mockMvc.perform(get("/account/get"));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfAccount.size())));

    }

    @Test
    void getAccountDetailsWithNegativeScenarioIfAccountDetailsNotFound() throws Exception {
        List<AccountDto> listOfAccount = new ArrayList<>();
        listOfAccount.add(AccountDto.builder().accountNumber(12).bankName("HDFC Bank").typeOfAccount("saving").totalAmount(567.00).build());
        given(accountServices.getAccountDetails()).willReturn(List.of());
        ResultActions response = mockMvc.perform(get("/account/get"));
        response.andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findAccountDetailsById() throws Exception {
        Integer accountNumber = 1;
        AccountDto accountDto = AccountDto.builder()
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
    void findAccountDetailsByIdWithNegativeScenarioIfIdNotExist() throws Exception {
        Integer accountNumber = 4;
        when(accountServices.getAccountDetailsByNumber(accountNumber)).thenReturn(Optional.empty());
        ResultActions response = mockMvc.perform(get("/account/get/{id}", accountNumber));
        response
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateAccountDetails() throws Exception {
        Integer accountNumber = 1;
        AccountDto updatedAccountDto = AccountDto.builder()
                .bankName("HDFC bank")
                .typeOfAccount("current")
                .totalAmount(600.00)
                .build();
        AccountResponse accountResponse = new AccountResponse("Account updated successfully", true);
        when(accountServices.updateAccount(updatedAccountDto, 1)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(put("/account/update/{accountNumber}", accountNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountResponse)));
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateAccountDetailsWithNegativeScenarioIfIdNotExist() throws Exception {
        Integer accountNumber = 3;
        AccountDto updatedAccountDto = AccountDto.builder()
                .bankName("HDFC bank")
                .typeOfAccount("current")
                .totalAmount(600.00)
                .accountStatus("Active")
                .build();
        AccountResponse accountResponse = new AccountResponse("Invalid account number", false);
        when(accountServices.updateAccount(updatedAccountDto, 3)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(put("/account/update/{accountNumber}", accountNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountResponse)));
        response.andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void deleteAccountDetailsById() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage("Account deleted successfully..");
        accountResponse.setSuccess(true);
        Integer accountNumber = 12;
        when(accountServices.deleteAccount(12)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();

    }

    @Test
    void deleteAccountDetailsByIdWithNegativeScenarioIfIdNotExist() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage("Invalid account number");
        accountResponse.setSuccess(false);
        Integer accountNumber = 12;
        when(accountServices.deleteAccount(12)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber));
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();
    }

    @Test
    void findTotalBalance() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage("Total balance : 6786.0");
        accountResponse.setSuccess(true);
        Integer accountNumber = 1;
        when(accountServices.getTotalBalance(1)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(get("/account/amount/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();
    }

    @Test
    void findTotalBalanceWithNegativeScenarioIfIdNotExist() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage("Invalid account number");
        accountResponse.setSuccess(false);
        Integer accountNumber = 98;
        when(accountServices.getTotalBalance(98)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(get("/account/amount/{accountNumber}", accountNumber));
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();
    }
}