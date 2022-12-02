package com.cognologix.BankSystemApplicationAssignment.converter;

import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter {
    @Autowired
    private ModelMapper modelMapper;
    //dto to model converter
    public Account dtoToModel(AccountDto accountDto){
        Account dtoAccount = this.modelMapper.map(accountDto, Account.class);
        return dtoAccount;
    }
    //model to dto converter
    public AccountDto modelToDto(Account account){
        AccountDto adto = this.modelMapper.map(account,AccountDto.class);
        return adto;
    }
}
