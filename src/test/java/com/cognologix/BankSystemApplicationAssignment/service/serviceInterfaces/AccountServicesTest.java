package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.AccountAlreadyExistException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.AccountNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
class AccountServicesTest {
    // uses the Log4j 2 classes to create the Logger object
    private static final Logger logger = LogManager.getLogger(AccountServicesTest.class);

    @Autowired
    private AccountServices accountServices;
    @MockBean
    private AccountRepo accountRepo;
    private AccountDto accountDto;
   @Autowired
   private Converter converter;
    @BeforeEach
    void setup(){
         accountDto= AccountDto.builder()
                .accountNumber(12)
                .bankName("SBI Bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
    }
    @Test
    void getAccountDetails() {
        when(accountRepo.findAll()).thenReturn((List<Account>) Stream
                .of(new Account( 3234,"Active","SBI Bank","Saving",20.00)).collect(Collectors.toList()));
        assertEquals(1, accountServices.getAccountDetails().size());
        logger.info("size of account is: "+accountServices.getAccountDetails().size());
    }
    @Test
    void getAccountDetailsWithNegativeScenarioIfAccountIsEmpty(){
        when(accountRepo.findAll()).thenReturn(Collections.emptyList());
        List<AccountDto>accountDtos=accountServices.getAccountDetails();
        Assertions.assertThat(accountDtos).isEmpty();
        assertEquals(0,accountDtos.size());
    }

    @Test
    void getAccountByNumber() {
        Account account=this.converter.accountDtoToModel(accountDto);
        when(accountRepo.existsById(1)).thenReturn(true);
        when(accountRepo.findById(1)).thenReturn(Optional.ofNullable(account));
        Optional<AccountDto> savedAccount = accountServices.getAccountDetailsByNumber(1);
        assertThat(savedAccount).isNotNull();
    }
    @Test
    void getAccountByNumberWithNegativeScenarioIfIdNotFound(){
        Account account=this.converter.accountDtoToModel(accountDto);
        when(accountRepo.findById(12)).thenReturn(Optional.ofNullable(account));
        assertThrows(AccountNotFoundException.class,
                ()->accountServices.getAccountDetailsByNumber(12));
        logger.error("error is: AccountNotFoundException");
    }
    @Test
    @DisplayName("create account")
    void createAccount() {
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(78)
                .bankName("SBI Bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        Account account = converter.accountDtoToModel(accountDto);
        when(accountRepo.findById(78)).thenReturn(Optional.of(account));
        when(accountRepo.save(account)).thenReturn(account);
        assertEquals("saving",account.getTypeOfAccount());
    }
    @Test
    void createAccountWithNegativeScenarioIfAccountAlreadyExist() {
         AccountDto accountDto= AccountDto.builder()
                .accountNumber(78)
                .bankName("SBI Bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        Account account=converter.accountDtoToModel(accountDto);
        when(accountRepo.findById(78)).thenReturn(Optional.of(account));
        when(accountRepo.save(account)).thenReturn(account);
        System.out.println(account);
        assertThrows(AccountAlreadyExistException.class, () ->
            accountServices.createAccount(accountDto)
        );
        }

@Test
void updateAccount(){
    Account account = this.converter.accountDtoToModel(accountDto);
    when(accountRepo.save(account)).thenReturn(account);
    accountDto.setBankName("SBI Bank");
    accountDto.setTypeOfAccount("current");
    accountDto.setTotalAmount(700.00);

    AccountResponse accountResponse = accountServices.updateAccount(accountDto,12);
    assertEquals("current",accountDto.getTypeOfAccount());

}
    @Test
    void updateAccountWithNegativeScenarioIfIdNotExist() {
        Account account = this.converter.accountDtoToModel(accountDto);
        when(accountRepo.save(account)).thenReturn(account);
        accountDto.setBankName("SBI Bank");
        accountDto.setTypeOfAccount("current");
        accountDto.setTotalAmount(700.00);

        AccountResponse accountResponse = accountServices.updateAccount(accountDto,1);
        assertEquals("Invalid account number",accountResponse.getMessage());
    }

    @Test
    void deleteAccount() {
        Integer accountId = 12;
        when(accountRepo.existsById(12)).thenReturn(true);
        AccountResponse accountResponse=accountServices.deleteAccount(accountId);
        assertEquals("Account deleted successfully..",accountResponse.getMessage());

    }

    @Test
    void deleteAccountWithNegativeScenarioIfIdNotexist() {
        Integer accountId = 67;
        when(accountRepo.existsById(67)).thenReturn(false);
        AccountResponse accountResponse=accountServices.deleteAccount(accountId);
        assertEquals("Invalid account number",accountResponse.getMessage());
    }

    @Test
    void getTotalBalance() {
        AccountResponse accountResponse=new AccountResponse("Total balance : 6786.0",true,null);
        Integer accountNumber=1;
        AccountResponse newAccountResponse=accountServices.getTotalBalance(accountNumber);
        System.out.println(newAccountResponse);
        assertEquals("Total balance : 6786.0",accountResponse.getMessage());
    }
    @Test
    void getTotalBalanceWithNegativeScenarioIfIdNotExist() {
        AccountResponse accountResponse=new AccountResponse("Invalid account number", false,null);
        Integer accountNumber=67;
        AccountResponse newAccountResponse=accountServices.getTotalBalance(accountNumber);
        System.out.println(newAccountResponse);
        assertEquals("Invalid account number",accountResponse.getMessage());
    }
}