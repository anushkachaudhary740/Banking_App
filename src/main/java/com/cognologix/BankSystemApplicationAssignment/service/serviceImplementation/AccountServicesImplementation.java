package com.cognologix.BankSystemApplicationAssignment.service.serviceImplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountMsgEnums;
import com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountType;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.AccountErrors;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.AccountStatusException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.customerException.CustomerNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.BalanceException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.IllegalTypeOfAccountException;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountStatusResponce;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.AccountServices;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountStatus.ACTIVATED;
import static com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountStatus.DEACTIVATED;

@Service
@Log4j2
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
        log.info(AccountMsgEnums.PRINT_ACCOUNT.getMessage());
        return accDtos;

    }

    @Override
    public Optional<AccountDto> getAccountDetailsByNumber(Integer accountNumber) {
        if(accountRepo.existsById(accountNumber)) {
            Account account = this.accountRepo.findById(accountNumber).get();
            log.info(AccountMsgEnums.PRINT_ACCOUNT_DETAILS_FOR_ONE_ACCOUNT.getMessage());
            return Optional.ofNullable(this.converter.accountModelToDto(Optional.ofNullable(account)));

        }
        else {
            log.error(AccountErrors.ACCOUNT_NOT_AVAILABLE.getMessage() +" with Id:"+accountNumber);
            throw new AccountNotFoundException("Account","Number",accountNumber);
        }

    }
        @Override
        public AccountResponse createAccount (AccountDto accountDto){
                String accountType;
                try {
                    String accountTypeOfCustomer = AccountType.valueOf(accountDto.getTypeOfAccount().toUpperCase()).toString();
                    accountType=accountTypeOfCustomer;
                } catch (Exception exception) {
                    throw new IllegalTypeOfAccountException("Invalid","type",accountDto.getTypeOfAccount());
                }
                Customer customer = customerRepo.findByCustomerId(accountDto.getCustomerId());
                if (customer == null) {
                    throw new CustomerNotFoundException("Customer","Id",accountDto.getCustomerId());
                }
                Double balance = accountDto.getTotalAmount();
               Account newAccount = new Account(accountDto.getAccountNumber(),customer.getCustomerName(),accountDto.getAccountStatus(),accountDto.getBankName() , accountType, balance,customer);
               accountRepo.save(newAccount);
                AccountResponse createdAccountResponse = new AccountResponse(AccountMsgEnums.CREATE_ACCOUNT.getMessage(), true, this.converter.accountModelToDto(Optional.of(newAccount)));
                log.info(AccountMsgEnums.CREATE_ACCOUNT.getMessage());
                return createdAccountResponse;

    }
        @Override
        public AccountResponse updateAccount (AccountDto accountDto, Integer accountNumber){
            if (accountRepo.existsById(accountNumber)) {
                Account accountDetails = this.converter.accountDtoToModel(accountDto);
                accountDetails.setAccountNumber(accountNumber);
                this.accountRepo.save(accountDetails);
                AccountResponse accountResponse = new AccountResponse(AccountMsgEnums.UPDATE_ACCOUNT.getMessage(), true,accountDto);
                log.info(AccountMsgEnums.UPDATE_ACCOUNT.getMessage());
                return accountResponse;
            } else {
                log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: "+accountNumber);
                return new AccountResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: "+accountNumber, false,null);

            }
        }
        @Override
        public AccountResponse getTotalBalance (Integer accountNumber){
            if (accountRepo.existsById(accountNumber)) {
                Optional<Account> list1 = accountRepo.findById(accountNumber);
                Account Balance1 = list1.get();
                Double totalBalance = Balance1.getTotalAmount();
                this.converter.accountModelToDto(Optional.of(Balance1));
                AccountResponse accountResponse = new AccountResponse(AccountMsgEnums.AVAILABLE_BALANCE.getMessage()+ totalBalance, true,null);
                log.info(AccountMsgEnums.AVAILABLE_BALANCE.getMessage() + totalBalance);
                return accountResponse;
            } else {
                log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: "+accountNumber);
                return new AccountResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: "+accountNumber, false,null);
            }
        }
        @Override
        public AccountResponse deleteAccount (Integer accountNumber){
            if (accountRepo.existsById(accountNumber)) {
                this.accountRepo.deleteById(accountNumber);
                AccountResponse accountResponse = new AccountResponse(AccountMsgEnums.DELETE_ACCOUNT.getMessage(), true,null);
                log.info(AccountMsgEnums.DELETE_ACCOUNT.getMessage());
                return accountResponse;
            } else {
                log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: "+accountNumber);
                return new AccountResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage()+" with Id: "+accountNumber, false,null);
            }
        }

    @Override
    public AccountStatusResponce activateAccount(Integer accountNumber) {
        if(accountRepo.existsById(accountNumber)) {
            Account account = this.accountRepo.findById(accountNumber).get();
            if (account.getAccountStatus().equals(ACTIVATED.toString())) {
                log.error(AccountErrors.ACCOUNT_ALREADY_ACTIVATE.getMessage());
                throw new AccountStatusException();
            }
            account.setAccountStatus(ACTIVATED.toString());
            this.accountRepo.save(account);
            AccountStatusResponce activateAccountResponse = new AccountStatusResponce(AccountMsgEnums.ACTIVATED_ACCOUNT.getMessage(), true);
            log.info(activateAccountResponse.toString());
            return activateAccountResponse;
        }else {
            log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage() +" with Id: "+accountNumber);
            throw new AccountNotFoundException("Account","Id",accountNumber);
        }
    }

    @Override
    public AccountStatusResponce deactivateAccount(Integer accountNumber) {
         if (accountRepo.existsById(accountNumber)) {
                Account account = this.accountRepo.findById(accountNumber).get();
                Double totalBalance = account.getTotalAmount();
                if (totalBalance > 0) {
                    log.error(AccountMsgEnums.BALANCE_INFO.getMessage());
                    throw new BalanceException(AccountMsgEnums.BALANCE_INFO.getMessage());
                }
                if (account.getAccountStatus().equals(DEACTIVATED.toString())) {
                    log.error(AccountErrors.ACCOUNT_ALREADY_ACTIVATE.getMessage());
                    throw new AccountStatusException();
                }
                account.setAccountStatus(DEACTIVATED.toString());
                this.accountRepo.save(account);

                AccountStatusResponce deactivateAccountResponse = new AccountStatusResponce(AccountMsgEnums.DEACTIVATE_ACCOUNT.getMessage(), true);
                log.info(deactivateAccountResponse.toString());
                return deactivateAccountResponse;

            }else {
                log.error(AccountErrors.ACCOUNT_NOT_FOUND +" with Id: "+accountNumber);
                throw new AccountNotFoundException("Account","Id",accountNumber);
            }
    }
}

