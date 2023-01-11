package com.cognologix.BankSystemApplicationAssignment.controllers;

import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountMsgEnums;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.AccountErrors;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountStatusResponce;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.AccountServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
        log.info("Starting...");
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(28)
                .bankName("sbi bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        AccountResponse accountResponse = new AccountResponse(AccountMsgEnums.CREATE_ACCOUNT.getMessage(), true,accountDto);
        when(accountServices.createAccount(accountDto)).thenReturn(accountResponse);

        ResultActions response = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)));
        response.andExpect(status().isCreated())
                .andReturn();
        log.info(accountDto.getAccountNumber());
        log.info("Completed createAccount testing...");
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
        log.info("Size of list is: " + listOfAccount.size());

    }

    @Test
    void getAccountDetailsWithNegativeScenarioIfAccountDetailsNotFound() throws Exception {
        List<AccountDto> listOfAccount = new ArrayList<>();
        given(accountServices.getAccountDetails()).willReturn(List.of());
        ResultActions response = mockMvc.perform(get("/account/get"));
        response.andExpect(status().isNotFound())
                .andReturn();
        log.error("Size of list is: " + listOfAccount.size());

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
        log.info("Bank Name: " + accountDto.getBankName() + ", type Of Account: " + accountDto.getTypeOfAccount() + ", Total Amount :" + accountDto.getTotalAmount());
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
        log.info("Starting....");
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(1)
                .bankName("sbi bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        AccountDto updatedAccountDto = AccountDto.builder()
                .bankName("HDFC bank")
                .typeOfAccount("current")
                .totalAmount(600.00)
                .build();
        AccountResponse accountResponse = new AccountResponse(AccountMsgEnums.UPDATE_ACCOUNT.getMessage(), true,accountDto);
        when(accountServices.updateAccount(updatedAccountDto, 1)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(put("/account/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountResponse)));
        response.andExpect(status().isOk())
                .andDo(print());
        log.info(accountResponse.getMessage());
        log.info("Completed...");
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
        AccountResponse accountResponse = new AccountResponse(AccountErrors.INVALID_ACCOUNT_NUMBER.getMessage(), false, null);
        log.error(accountResponse.getMessage());
        when(accountServices.updateAccount(updatedAccountDto, 3)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(put("/account/update/{accountNumber}", accountNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountResponse)));
        response.andDo(print())
                .andReturn();
    }

    @Test
    void deleteAccountDetailsById() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage(AccountMsgEnums.DELETE_ACCOUNT.getMessage());
        accountResponse.setSuccess(true);
        accountResponse.setAccount(null);
        Integer accountNumber = 12;
        when(accountServices.deleteAccount(12)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();
        log.info(accountResponse.getMessage());
    }

    @Test
    void deleteAccountDetailsByIdWithNegativeScenarioIfIdNotExist() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage(AccountErrors.INVALID_ACCOUNT_NUMBER.getMessage());
        accountResponse.setSuccess(false);
        accountResponse.setAccount(null);
        Integer accountNumber = 12;
        when(accountServices.deleteAccount(12)).thenReturn(accountResponse);
        log.error(accountResponse.getMessage());
        ResultActions response = mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber));
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();
    }

    @Test
    void findTotalBalance() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage(AccountMsgEnums.AVAILABLE_BALANCE.getMessage()+" 6786.0");
        accountResponse.setSuccess(true);
        accountResponse.setAccount(null);
        Integer accountNumber = 1;
        when(accountServices.getTotalBalance(1)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(get("/account/amount/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();
        log.info(accountResponse.getMessage());
    }

    @Test
    void findTotalBalanceWithNegativeScenarioIfIdNotExist() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setMessage(AccountErrors.INVALID_ACCOUNT_NUMBER.getMessage());
        accountResponse.setSuccess(false);
        accountResponse.setAccount(null);
        Integer accountNumber = 98;
        when(accountServices.getTotalBalance(98)).thenReturn(accountResponse);
        ResultActions response = mockMvc.perform(get("/account/amount/{accountNumber}", accountNumber));
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message", is(accountResponse.getMessage())))
                .andReturn();
        log.error(accountResponse.getMessage());
    }
    @Test
    void deactivateAccount() throws Exception {
        Integer accountNumber = 100;
        AccountStatusResponce deactivateAccountResponse = new AccountStatusResponce(AccountMsgEnums.DEACTIVATE_ACCOUNT.getMessage(), true);
        log.info(deactivateAccountResponse.toString());
        when(accountServices.deactivateAccount(accountNumber)).thenReturn(deactivateAccountResponse);
        ResultActions response = mockMvc.perform(put("/account/deactivate/{accountNumber}",accountNumber));
        response.andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void deactivateAccountWithNegativeScenarioIfAccountNumberNotExist() throws Exception {
        Integer accountNumber = 100;
        AccountStatusResponce deactivateAccountResponse = new AccountStatusResponce(AccountErrors.ACCOUNT_NOT_FOUND.getMessage() +" with Id : 100",false);
        log.error(deactivateAccountResponse.toString());
        when(accountServices.deactivateAccount(accountNumber)).thenReturn(deactivateAccountResponse);
        ResultActions response = mockMvc.perform(put("/account/deactivate/{accountNumber}",accountNumber));
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void activateAccount() throws Exception {
        Integer accountNumber = 100;
        AccountStatusResponce activateAccountResponse = new AccountStatusResponce(AccountMsgEnums.ACTIVATED_ACCOUNT.getMessage(), true);
        log.info(activateAccountResponse.toString());
        when(accountServices.activateAccount(accountNumber)).thenReturn(activateAccountResponse);
        ResultActions response = mockMvc.perform(put("/account/activate/{accountNumber}", accountNumber));
        response.andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void activateAccountWithNegativeScenarioIfAccountNumberNotExist() throws Exception {
        Integer accountNumber = 100;
        AccountStatusResponce activateAccountResponse = new AccountStatusResponce(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id : 100", false);
        log.error(activateAccountResponse.toString());
        when(accountServices.activateAccount(accountNumber)).thenReturn(activateAccountResponse);
        ResultActions response = mockMvc.perform(put("/account/activate/{accountNumber}", accountNumber));
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

}