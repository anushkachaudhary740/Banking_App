package com.cognologix.BankSystemApplicationAssignment.converter.accountconverter;

import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoToModelConverter {
    @Autowired
    private ModelMapper modelMapper;
    public Account dtoToModel(AccountDto accountDto){
        Account dtoAccount = this.modelMapper.map(accountDto, Account.class);
        return dtoAccount;
    }
}
