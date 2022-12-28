package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountServices {
    List<AccountDto> getAccountDetails();
    Optional<AccountDto> getAccountDetailsByNumber(Integer accountNumber);
    AccountDto createAccount(AccountDto account);
    AccountResponse updateAccount(AccountDto accountDto,Integer accountNumber);
    AccountResponse getTotalBalance(Integer accountNumber);
    AccountResponse deleteAccount(Integer accountNumber);

}
