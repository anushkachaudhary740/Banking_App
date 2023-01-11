package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountMsgEnums;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.AccountErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountStatusResponce;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Log4j2
class AccountServicesTest {
    @Autowired
    private AccountServices accountServices;
    @MockBean
    private AccountRepo accountRepo;
    private AccountDto accountDto;
    @Autowired
    private Converter converter;
    private Customer customer;

    @BeforeEach
    void setup() {
        accountDto = AccountDto.builder()
                .accountNumber(12)
                .bankName("SBI Bank")
                .typeOfAccount("SAVING")
                .totalAmount(500.00)
                .build();
    }

    @Test
    void getAccountDetails() {
        when(accountRepo.findAll()).thenReturn((List<Account>) Stream
                .of(new Account(3234, "Anu", "ACTIVATED", "Active", "SBI Bank", 20.00, customer)).collect(Collectors.toList()));
        assertEquals(1, accountServices.getAccountDetails().size());
        log.info("size of account is: " + accountServices.getAccountDetails().size());
    }

    @Test
    void getAccountDetailsWithNegativeScenarioIfAccountIsEmpty() {
        when(accountRepo.findAll()).thenReturn(Collections.emptyList());
        List<AccountDto> accountDtos = accountServices.getAccountDetails();
        Assertions.assertThat(accountDtos).isEmpty();
        assertEquals(0, accountDtos.size());
    }

    @Test
    void getAccountByNumber() {
        Account account = this.converter.accountDtoToModel(accountDto);
        when(accountRepo.existsById(1)).thenReturn(true);
        when(accountRepo.findById(1)).thenReturn(Optional.ofNullable(account));
        Optional<AccountDto> savedAccount = accountServices.getAccountDetailsByNumber(1);
        assertThat(savedAccount).isNotNull();
    }

    @Test
    void getAccountByNumberWithNegativeScenarioIfIdNotFound() {
        Account account = this.converter.accountDtoToModel(accountDto);
        when(accountRepo.findById(12)).thenReturn(Optional.ofNullable(account));
        assertThrows(AccountNotFoundException.class,
                () -> accountServices.getAccountDetailsByNumber(12));
        log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("create account")
    void createAccount() {
        AccountDto accountDto = AccountDto.builder()
                .customerId(1)
                .accountNumber(78)
                .bankName("SBI Bank")
                .typeOfAccount("savings")
                .totalAmount(500.00)
                .build();
        Account account = converter.accountDtoToModel(accountDto);
        when(accountRepo.findById(78)).thenReturn(Optional.of(account));
        when(accountRepo.save(account)).thenReturn(account);
        AccountResponse newAccount = accountServices.createAccount(accountDto);
        assertEquals(AccountMsgEnums.CREATE_ACCOUNT.getMessage(), newAccount.getMessage());
    }

    @Test
    void createAccountWithNegativeScenarioIfAccountAlreadyExist() {
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(78)
                .bankName("SBI Bank")
                .typeOfAccount("SAVINGS")
                .totalAmount(500.00)
                .build();
        Account account = converter.accountDtoToModel(accountDto);
        when(accountRepo.findById(78)).thenReturn(Optional.of(account));
        when(accountRepo.save(account)).thenReturn(account);
        System.out.println(account);
        assertThrows(RuntimeException.class, () ->
                accountServices.createAccount(accountDto)
        );
    }

    @Test
    void updateAccount() {
        Account account = this.converter.accountDtoToModel(accountDto);
        when(accountRepo.save(account)).thenReturn(account);
        accountDto.setBankName("SBI Bank");
        accountDto.setTypeOfAccount("current");
        accountDto.setTotalAmount(700.00);

        AccountResponse accountResponse = accountServices.updateAccount(accountDto, 12);
        assertEquals("current", accountDto.getTypeOfAccount());

    }

    @Test
    void updateAccountWithNegativeScenarioIfIdNotExist() {
        Account account = this.converter.accountDtoToModel(accountDto);
        when(accountRepo.save(account)).thenReturn(account);
        accountDto.setBankName("SBI Bank");
        accountDto.setTypeOfAccount("current");
        accountDto.setTotalAmount(700.00);

        AccountResponse accountResponse = accountServices.updateAccount(accountDto, 1);
        assertEquals(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: 1", accountResponse.getMessage());
    }

    @Test
    void deleteAccount() {
        Integer accountId = 12;
        when(accountRepo.existsById(12)).thenReturn(true);
        AccountResponse accountResponse = accountServices.deleteAccount(accountId);
        assertEquals(AccountMsgEnums.DELETE_ACCOUNT.getMessage(), accountResponse.getMessage());

    }

    @Test
    void deleteAccountWithNegativeScenarioIfIdNotexist() {
        Integer accountId = 67;
        when(accountRepo.existsById(67)).thenReturn(false);
        AccountResponse accountResponse = accountServices.deleteAccount(accountId);
        assertEquals(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: "+accountId, accountResponse.getMessage());
    }

    @Test
    void getTotalBalance() {
        AccountResponse accountResponse = new AccountResponse(AccountMsgEnums.AVAILABLE_BALANCE.getMessage()+" 6786.0", true, null);
        Integer accountNumber = 1;
        AccountResponse newAccountResponse = accountServices.getTotalBalance(accountNumber);
        System.out.println(newAccountResponse);
        assertEquals(AccountMsgEnums.AVAILABLE_BALANCE.getMessage()+" 6786.0", accountResponse.getMessage());
    }

    @Test
    void getTotalBalanceWithNegativeScenarioIfIdNotExist() {
        AccountResponse accountResponse = new AccountResponse(AccountErrors.INVALID_ACCOUNT_NUMBER.getMessage(), false, null);
        Integer accountNumber = 67;
        AccountResponse newAccountResponse = accountServices.getTotalBalance(accountNumber);
        System.out.println(newAccountResponse);
        assertEquals(AccountErrors.INVALID_ACCOUNT_NUMBER.getMessage(), accountResponse.getMessage());
    }

    @Test
    void activateAccount() {
        accountDto.setAccountStatus("DEACTIVATE");
        Account account = converter.accountDtoToModel(accountDto);
        Integer accountNumber = 1;
        when(accountRepo.existsById(accountNumber)).thenReturn(true);
        when(accountRepo.findById(accountNumber)).thenReturn(Optional.ofNullable(account));
        when(accountRepo.save(account)).thenReturn(account);
        AccountStatusResponce accountStatusResponce = accountServices.activateAccount(accountNumber);
        assertEquals(AccountMsgEnums.ACTIVATED_ACCOUNT.getMessage(), accountStatusResponce.getMessage());
    }

    @Test
    void activateAccountWithNegativeScenarioIfAccountNotExist() {
        accountDto.setAccountStatus("DEACTIVATE");
        Account account = converter.accountDtoToModel(accountDto);
        Integer accountNumber = 90;
        when(accountRepo.existsById(accountNumber)).thenReturn(false);
        assertThrows(AccountNotFoundException.class, () ->
                accountServices.activateAccount(accountNumber)
        );
    }

    @Test
    void deactivateAccount() {
        accountDto.setTotalAmount(0.0);
        accountDto.setAccountStatus("ACTIVATE");
        Account account = converter.accountDtoToModel(accountDto);
        Integer accountNumber = 7;
        when(accountRepo.existsById(accountNumber)).thenReturn(true);
        when(accountRepo.findById(accountNumber)).thenReturn(Optional.ofNullable(account));
        when(accountRepo.save(account)).thenReturn(account);
        AccountStatusResponce accountStatusResponce = accountServices.deactivateAccount(accountNumber);
        assertEquals(AccountMsgEnums.DEACTIVATE_ACCOUNT.getMessage(), accountStatusResponce.getMessage());

    }

    @Test
    void deactivateAccountWithNegativeScenarioIfAccountNumberNotExist() {
        accountDto.setTotalAmount(0.0);
        accountDto.setAccountStatus("ACTIVATE");
        Account account = converter.accountDtoToModel(accountDto);
        Integer accountNumber = 78;
        when(accountRepo.existsById(accountNumber)).thenReturn(false);
        assertThrows(AccountNotFoundException.class, () -> accountServices.deactivateAccount(accountNumber));
    }
}