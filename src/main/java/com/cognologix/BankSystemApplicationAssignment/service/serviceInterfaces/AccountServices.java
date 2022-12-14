package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountServices {
    //void saveAccount(Account account);
    List<AccountDto> getAccountDetails();
    Optional<AccountDto> getAccountDetailsByNumber(Integer accountNumber);
    AccountDto createAccount(AccountDto account);
    AccountDto updateAccount(AccountDto accountDto);
    AccountResponse getTotalBalance(Integer accountNumber);
    AccountResponse deleteAccount(Integer accountNumber);

}
