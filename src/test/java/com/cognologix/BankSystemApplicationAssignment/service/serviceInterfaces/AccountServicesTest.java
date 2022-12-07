package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.AccountConverter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
   private AccountConverter accountConverter;
  // @Autowired
    //private ModelMapper modelMapper;

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
    void getAccountByNumber() {
        Account account=this.accountConverter.dtoToModel(accountDto);
        given(accountRepo.findById(12)).willReturn(Optional.ofNullable(account));
        AccountDto accountDto1=this.accountConverter.modelToDto(account);
        // when

        Optional<AccountDto> savedAccount = accountServices.getAccountDetailsByNumber(accountDto1.getAccountNumber());
        // then
        assertThat(savedAccount).isNotNull();

    }



    @Test
    void createAccount() {
//        Account account=this.accountConverter.dtoToModel(accountDto);
////        given(accountRepo.findById(account.getAccountNumber()))
////                .willReturn(Optional.empty());
//        given(accountRepo.save(account)).willReturn(account);
//        System.out.println("account......"+account);
//        AccountDto accountDto1=this.accountConverter.modelToDto(account);
//        AccountDto savedAccount = accountServices.createAccount(accountDto1);
//        //System.out.println("savedAccount"+savedAccount);
//        assertThat(savedAccount).isNotNull();
    }

    @Test
    void updateAccount() {
        // given - setup
        Account account = this.accountConverter.dtoToModel(accountDto);
        given(accountRepo.save(account)).willReturn(account);
        accountDto.setBankName("SBI Bank");
        accountDto.setTypeOfAccount("current");
        accountDto.setTotalAmount(700.00);
        // when -  action or the behaviour that we are going test
        AccountDto updateAccount = accountServices.updateAccount(accountDto);
        // then - verify the output
        Assertions.assertThat(updateAccount.getTypeOfAccount()).isEqualTo("current");
        Assertions.assertThat(updateAccount.getBankName()).isEqualTo("SBI Bank");
    }

    @Test
    void deleteAccount() {
        // given - precondition or setup
        Integer accountId = 12;
        willDoNothing().given(accountRepo).deleteById(accountId);
        // when -  action or the behaviour that we are going test
        accountServices.deleteAccount(accountId);
        // then - verify the output
        verify(accountRepo, times(1)).deleteById(accountId);

    }
}