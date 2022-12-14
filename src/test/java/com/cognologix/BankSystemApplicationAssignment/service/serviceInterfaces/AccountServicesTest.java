package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountServicesTest {
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
                .of(new Account( 3234,"Active ","SBI Bank", "saving",700.0)).collect(Collectors.toList()));
        assertEquals(1, accountServices.getAccountDetails().size());
    }
    @Test
    void accountDetailsWithNegativeScenario(){
        when(accountRepo.findAll()).thenReturn(Collections.emptyList());
        List<AccountDto>accountDtos=accountServices.getAccountDetails();
        Assertions.assertThat(accountDtos).isEmpty();
        assertEquals(0,accountDtos.size());
    }

    @Test
    void getAccountByNumber() {
        Account account=this.converter.accountDtoToModel(accountDto);
        given(accountRepo.findById(12)).willReturn(Optional.ofNullable(account));
        AccountDto accountDto1=this.converter.accountModelToDto(account);
        // when
        Optional<AccountDto> savedAccount = accountServices.getAccountDetailsByNumber(accountDto1.getAccountNumber());
        // then
        assertThat(savedAccount).isNotNull();

    }
    @Test
    void createAccount() {
         AccountDto accountDto= AccountDto.builder()
                .accountNumber(12)
                .bankName("SBI Bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
        Account account=converter.accountDtoToModel(accountDto);
        when(accountRepo.findById(12)).thenReturn(Optional.of(account));
        when(accountRepo.save(account)).thenReturn(account);
        System.out.println(account);
        //assertThat(accountDto1).isNotNull();
        assertThrows(ResourceNotFoundException.class, () -> {
            accountServices.createAccount(accountDto);
        });
        verify(accountRepo, never()).save(any(Account.class));
}

    @Test
    void updateAccount() {
        Account account = this.converter.accountDtoToModel(accountDto);
        given(accountRepo.save(account)).willReturn(account);
        accountDto.setBankName("SBI Bank");
        accountDto.setTypeOfAccount("current");
        accountDto.setTotalAmount(700.00);
        AccountDto updateAccount = accountServices.updateAccount(accountDto);
        Assertions.assertThat(updateAccount.getTypeOfAccount()).isEqualTo("current");
        Assertions.assertThat(updateAccount.getBankName()).isEqualTo("SBI Bank");
    }

    @Test
    void deleteAccount() {
        Integer accountId = 12;
        willDoNothing().given(accountRepo).deleteById(accountId);
        accountServices.deleteAccount(accountId);
        verify(accountRepo, times(1)).deleteById(accountId);

    }
}