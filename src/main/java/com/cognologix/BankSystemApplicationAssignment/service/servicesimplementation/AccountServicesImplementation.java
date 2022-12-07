package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.AccountConverter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
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
    private AccountConverter accountConverter;
    
    @Override
    public List<AccountDto> getAccountDetails() {

        List<Account> list=this.accountRepo.findAll();
        List<AccountDto> accDtos =list.stream().map(e-> this.accountConverter.modelToDto(e))
                .collect(Collectors.toList());
        return accDtos;
    }
    @Override
    public Optional<AccountDto> getAccountDetailsByNumber(Integer accountNumber) {
        Account account=this.accountRepo.findById(accountNumber).orElseThrow(()->new ResourceNotFoundException("Account","Number",accountNumber));
        return Optional.ofNullable(this.accountConverter.modelToDto(account));
    }
    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Integer accNum=1000000000;
        accountDto.setAccountNumber(accountDto.getAccountNumber()+accNum);
        Account accountDetails = this.accountConverter.dtoToModel(accountDto);

        Account acc = this.accountRepo.save(accountDetails);
        return this.accountConverter.modelToDto(acc);
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        Account accountDetails = this.accountConverter.dtoToModel(accountDto);
        //accountDetails.setAccountNumber(accountNumber);
        this.accountRepo.save(accountDetails);
        return this.accountConverter.modelToDto(accountDetails);

    }

    @Override
    public Double getTotalBalance(Integer accountNumber) {
        Optional<Account> list1 = accountRepo.findById(accountNumber);
        Account Balance1 = list1.get();
        Double totalBalance = Balance1.getTotalAmount();
        this.accountConverter.modelToDto(Balance1);
        return totalBalance;
    }

    @Override
    public void deleteAccount(Integer accountNumber) {
        this.accountRepo.deleteById(accountNumber);
    }
}
