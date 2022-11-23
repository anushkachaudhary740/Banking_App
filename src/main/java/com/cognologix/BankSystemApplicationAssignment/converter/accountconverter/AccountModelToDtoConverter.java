package com.cognologix.BankSystemApplicationAssignment.converter.accountconverter;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountModelToDtoConverter {
    @Autowired
    private ModelMapper modelMapper;
    public AccountDto modelToDto(Account account){
        AccountDto adto = this.modelMapper.map(account,AccountDto.class);
        return adto;
    }
}
