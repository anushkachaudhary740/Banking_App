package com.cognologix.BankSystemApplicationAssignment.converter;

import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    @Autowired
    private ModelMapper modelMapper;
    //Account dto to model converter
    public Account accountDtoToModel(AccountDto accountDto){
        Account dtoAccount = this.modelMapper.map(accountDto, Account.class);
        return dtoAccount;
    }
    //Account model to dto converter
    public AccountDto accountModelToDto(Account account){
        AccountDto adto = this.modelMapper.map(account,AccountDto.class);
        return adto;
    }
    // customer dto to model converter
    public Customer customerDtoToModel(CustomerDto customerDto) {
        Customer dtoCustomer = this.modelMapper.map(customerDto, Customer.class);
        return dtoCustomer;
    }
    // customer model to dto converter
    public CustomerDto customerModelToDto(Customer customer) {
        CustomerDto cdto = this.modelMapper.map(customer, CustomerDto.class);
        return cdto;
    }
    //Transaction dto to model converter
    public Transaction transferDtoToModel(TransactionDto TransactionDto){
        Transaction transModel=this.modelMapper.map(TransactionDto,Transaction.class);
        return transModel;
    }
    //model to dto converter
    public TransactionDto transferModelToDto(Transaction transaction){
        TransactionDto transDto=this.modelMapper.map(transaction, TransactionDto.class);
        return transDto;
    }
    //for deposit and withdraw converter
    public Transaction transactionDtoToModel(TransactionDto transactionDto){
        Transaction transModel=this.modelMapper.map(transactionDto,Transaction.class);
        return transModel;
    }
    public TransactionDto transactionModelToDto(Transaction transaction){
        TransactionDto transDto=this.modelMapper.map(transaction, TransactionDto.class);
        return transDto;
    }
}
