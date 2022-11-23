package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.accountconverter.AccountDtoToModelConverter;
import com.cognologix.BankSystemApplicationAssignment.converter.accountconverter.AccountModelToDtoConverter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.service.services.AccountServices;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServicesImplementation implements AccountServices {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AccountDtoToModelConverter accountDtoToModelConverter;
    @Autowired
    private AccountModelToDtoConverter accountModelToDtoConverter;

    @Override
    public List<AccountDto> getAccountDetails() {
        List<Account> list=this.accountRepo.findAll();
        List<AccountDto> accDtos =list.stream().map(e-> this.accountModelToDtoConverter.modelToDto(e))
                .collect(Collectors.toList());
        return accDtos;
    }
    @Override
    public AccountDto getAccountByNumber(Integer accountNumber) {
        Account account=this.accountRepo.findById(accountNumber).orElseThrow(()->new ResourceNotFoundException("Account","Number",accountNumber));
        return this.accountModelToDtoConverter.modelToDto(account);
    }
    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account accountDetails = this.accountDtoToModelConverter.dtoToModel(accountDto);
        Account acc = this.accountRepo.save(accountDetails);
        return this.accountModelToDtoConverter.modelToDto(acc);
    }

    @Override
    public void updateAccount(AccountDto accountDto, Integer accountNumber) {
        Account accountDetails = this.accountDtoToModelConverter.dtoToModel(accountDto);
        accountDetails.setAccountNumber(accountNumber);
        this.accountRepo.save(accountDetails);
        this.accountModelToDtoConverter.modelToDto(accountDetails);
    }
    @Override
    public void deleteAccount(Integer accountNumber) {
        this.accountRepo.deleteById(accountNumber);
    }
}
