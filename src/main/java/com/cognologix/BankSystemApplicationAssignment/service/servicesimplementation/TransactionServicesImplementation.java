package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.AccountNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.InSufficientBalanceException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TransactionServicesImplementation implements TransactionServices {
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private Converter transactionConverter;
    @Autowired
    private AccountRepo accountRepo;
    Random random=new Random();
    @Override
    public List<TransactionDto> getTransactionDetails() {
        List<Transaction> list = this.transactionsRepo.findAll();
        List<TransactionDto> transDtos = list.stream().map(e -> this.transactionConverter.transferModelToDto(e))
                .collect(Collectors.toList());
        return transDtos;
    }

    @Override
    public Optional<TransactionDto> getTransactionDetailsById(Integer transactionId) {
        if(transactionsRepo.existsById(transactionId)) {
            Transaction transaction = this.transactionsRepo.findById(transactionId).get();
            return Optional.ofNullable(this.transactionConverter.transferModelToDto(transaction));
        }
        else {
            throw new ResourceNotFoundException("transaction", "Id", transactionId);
        }
    }

    @Override
    public TransactionsResponse depositAmount(Integer accountNumber, Double depositAmount) {
         TransactionDto trans=new TransactionDto();
         if(accountRepo.existsById(accountNumber)) {
             Account account=accountRepo.findById(accountNumber).get();
             trans.setToAccountNumber(accountNumber);
             trans.setTransferAmount(depositAmount);
             trans.setStatus("amount deposited");
             trans.setDate(LocalDate.now());
             trans.setTime(LocalTime.now());
             Transaction trans1 = this.transactionConverter.transactionDtoToModel(trans);
             trans1.setFromAccountNumber(0);
             this.transactionsRepo.save(trans1);
             account.setTotalAmount(account.getTotalAmount() + depositAmount);
             this.accountRepo.save(account);
             TransactionsResponse transactionsResponse =new TransactionsResponse("Rs "+depositAmount+" successfully deposit.....",true);
             return transactionsResponse;
         }
         else {
             TransactionsResponse transactionsResponse  =new TransactionsResponse("Account does not exist",false);
             return transactionsResponse;
         }
    }

    @Override
    public TransactionsResponse withdrawAmount(Integer accountNumber, Double withdrawAmount) {
        if(accountRepo.existsById(accountNumber)) {
        Account account=accountRepo.findById(accountNumber).get();
        TransactionDto trans= new TransactionDto();
            if (account.getTotalAmount() >= withdrawAmount) {
                trans.setToAccountNumber(accountNumber);
                trans.setTransferAmount(withdrawAmount);
                trans.setStatus("amount withdrew");
                trans.setDate(LocalDate.now());
                trans.setTime(LocalTime.now());
                Transaction trans1 = this.transactionConverter.transactionDtoToModel(trans);
                trans1.setFromAccountNumber(0);
                this.transactionsRepo.save(trans1);
                account.setTotalAmount(account.getTotalAmount() - withdrawAmount);
                this.accountRepo.save(account);
                TransactionsResponse transactionsResponse = new TransactionsResponse("Rs " + withdrawAmount + " successfully withdraw.....", true);
                return transactionsResponse;
            } else {
                throw new InSufficientBalanceException(" Insufficient Amount.....");
            }
        }else {
            return new TransactionsResponse("Account does not exist",false);
        }
    }
    @Override
    public TransactionsResponse transferAmount(Integer senderAccountNumber, Integer receiverAccountNumber, Double amount) {
        if(accountRepo.existsById(senderAccountNumber)&& accountRepo.existsById(receiverAccountNumber)) {
            Account account = accountRepo.findById(senderAccountNumber).get();
                    //.orElseThrow(() -> new ResourceNotFoundException("account", "number", senderAccountNumber));
            Account account1 = accountRepo.findById(receiverAccountNumber).get();
                    //.orElseThrow(() -> new ResourceNotFoundException("account", "number", receiverAccountNumber));
            TransactionDto trans = new TransactionDto();
            if (account.getTotalAmount() >= amount) {
                trans.setFromAccountNumber(senderAccountNumber);
                trans.setTransferAmount(amount);
                trans.setDate(LocalDate.now());
                trans.setTime(LocalTime.now());
                trans.setStatus(amount + " Rs withdrew from account " + senderAccountNumber + " and deposited in account " + receiverAccountNumber);
                Transaction trans1 = this.transactionConverter.transferDtoToModel(trans);
                account.setTotalAmount(account.getTotalAmount() - amount);
                this.accountRepo.save(account);
            } else {
                throw new InSufficientBalanceException(" Insufficient Amount.....");
            }
            trans.setToAccountNumber(receiverAccountNumber);
            Transaction trans1 = this.transactionConverter.transferDtoToModel(trans);
            this.transactionsRepo.save(trans1);
            account1.setTotalAmount(account1.getTotalAmount() + amount);
            this.accountRepo.save(account1);
            TransactionsResponse transactionsResponse = new TransactionsResponse("Rs " + amount + " successfully transfer.....", true);
            return transactionsResponse;
        }else{
            return new TransactionsResponse("Account does not exist",false);
        }

    }
}
