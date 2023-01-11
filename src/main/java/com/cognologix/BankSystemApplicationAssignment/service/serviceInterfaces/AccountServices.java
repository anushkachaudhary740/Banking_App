package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountStatusResponce;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountServices {
    List<AccountDto> getAccountDetails();
    Optional<AccountDto> getAccountDetailsByNumber(Integer accountNumber);
    AccountResponse createAccount(AccountDto account);
    AccountResponse updateAccount(AccountDto accountDto,Integer accountNumber);
    AccountResponse getTotalBalance(Integer accountNumber);
    AccountResponse deleteAccount(Integer accountNumber);
    AccountStatusResponce activateAccount(Integer accountNumber);
    AccountStatusResponce deactivateAccount(Integer accountNumber);

}
