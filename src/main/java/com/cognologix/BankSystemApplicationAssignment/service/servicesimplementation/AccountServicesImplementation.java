package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.AccountServices;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServicesImplementation implements AccountServices {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    //private AccountDtoToModelConverter accountDtoToModelConverter;
    private Converter converter;
    @Override
    public List<AccountDto> getAccountDetails() {

        List<Account> list=this.accountRepo.findAll();
        List<AccountDto> accDtos =list.stream().map(e-> this.converter.accountModelToDto(e))
                .collect(Collectors.toList());
        return accDtos;
    }
    @Override
    public Optional<AccountDto> getAccountDetailsByNumber(Integer accountNumber) {
        Account account=this.accountRepo.findById(accountNumber).orElseThrow(()->new ResourceNotFoundException("Account","Number",accountNumber));
        return Optional.ofNullable(this.converter.accountModelToDto(account));
    }
    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account accountDetails = this.converter.accountDtoToModel(accountDto);
        Optional<Account> savedAccount = accountRepo.findById(accountDetails.getAccountNumber());
        if(savedAccount.isPresent()){
            throw new ResourceNotFoundException("Account","AccountNumber",accountDetails.getAccountNumber());
        }
        Account acc = this.accountRepo.save(accountDetails);
        return this.converter.accountModelToDto(acc);
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        Account accountDetails = this.converter.accountDtoToModel(accountDto);
        //accountDetails.setAccountNumber(accountNumber);
        this.accountRepo.save(accountDetails);
        return this.converter.accountModelToDto(accountDetails);

    }

    @Override
    public AccountResponse getTotalBalance(Integer accountNumber) {
        Optional<Account> list1 = accountRepo.findById(accountNumber);
        Account Balance1 = list1.get();
        Double totalBalance = Balance1.getTotalAmount();
        this.converter.accountModelToDto(Balance1);
        AccountResponse accountResponse=new AccountResponse("Total balance : "+totalBalance,true);
        return accountResponse;

    }

    @Override
    public AccountResponse deleteAccount(Integer accountNumber) {
        this.accountRepo.deleteById(accountNumber);
        AccountResponse accountResponse=new AccountResponse("Account deleted successfully..",true);
        return accountResponse;
    }
}
