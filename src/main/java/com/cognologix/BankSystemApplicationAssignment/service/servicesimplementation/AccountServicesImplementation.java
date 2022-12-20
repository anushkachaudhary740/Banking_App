package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.AccountAlreadyExistException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.AccountNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.CustomerNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.AccountServices;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServicesImplementation implements AccountServices {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private Converter converter;
    @Autowired
    private CustomerRepo customerRepo;
    private Customer customer;

    @Override
    public List<AccountDto> getAccountDetails() {

        List<Account> list = this.accountRepo.findAll();
        List<AccountDto> accDtos = list.stream().map(e -> this.converter.accountModelToDto(Optional.ofNullable(e)))
                .collect(Collectors.toList());
        return accDtos;
    }

    @Override
    public Optional<AccountDto> getAccountDetailsByNumber(Integer accountNumber) {
        if(accountRepo.existsById(accountNumber)) {
            Account account = this.accountRepo.findById(accountNumber).get();
            return Optional.ofNullable(this.converter.accountModelToDto(Optional.ofNullable(account)));
        }
        else {
            throw new AccountNotFoundException("Account","Number",accountNumber);
        }

    }
        @Override
        public AccountDto createAccount (AccountDto accountDto){
            Account accountDetails = this.converter.accountDtoToModel(accountDto);
            Optional<Account> savedAccount = accountRepo.findById(accountDetails.getAccountNumber());
            if (savedAccount.isPresent()) {
                throw new AccountAlreadyExistException("Account", "AccountNumber", accountDetails.getAccountNumber());
            }
            return this.converter.accountModelToDto(Optional.of(accountDetails));

        }
        @Override
        public AccountResponse updateAccount (AccountDto accountDto, Integer accountNumber){
            if (accountRepo.existsById(accountNumber)) {
                Account accountDetails = this.converter.accountDtoToModel(accountDto);
                accountDetails.setAccountNumber(accountNumber);
                this.accountRepo.save(accountDetails);
                AccountResponse accountResponse = new AccountResponse("Account updated successfully", true);
                return accountResponse;
            } else {
                return new AccountResponse("Invalid account number", false);

            }
        }
        @Override
        public AccountResponse getTotalBalance (Integer accountNumber){
            if (accountRepo.existsById(accountNumber)) {
                Optional<Account> list1 = accountRepo.findById(accountNumber);
                Account Balance1 = list1.get();
                Double totalBalance = Balance1.getTotalAmount();
                this.converter.accountModelToDto(Optional.of(Balance1));
                AccountResponse accountResponse = new AccountResponse("Total balance : " + totalBalance, true);
                return accountResponse;
            } else {
                return new AccountResponse("Invalid account number", false);

            }
        }
        @Override
        public AccountResponse deleteAccount (Integer accountNumber){
            if (accountRepo.existsById(accountNumber)) {
                this.accountRepo.deleteById(accountNumber);
                AccountResponse accountResponse = new AccountResponse("Account deleted successfully..", true);
                return accountResponse;
            } else {
                return new AccountResponse("Invalid account number", false);
            }
        }
    }

