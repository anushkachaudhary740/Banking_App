package com.cognologix.BankSystemApplicationAssignment.service.services;

import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface AccountServices {
    //void saveAccount(Account account);
    List<AccountDto> getAccountDetails();
    AccountDto getAccountByNumber(Integer accountNumber);
    AccountDto createAccount(AccountDto account);
    void updateAccount(AccountDto accountDto,Integer accountNumber);
    void deleteAccount(Integer accountNumber);

}
