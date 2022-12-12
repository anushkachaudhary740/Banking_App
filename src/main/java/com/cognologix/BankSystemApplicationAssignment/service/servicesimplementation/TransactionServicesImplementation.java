package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.InSufficientBalanceException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.dto.AmountTransferDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    public List<AmountTransferDto> getTransactionDetails() {
        List<Transaction> list = this.transactionsRepo.findAll();
        List<AmountTransferDto> transDtos = list.stream().map(e -> this.transactionConverter.transferModelToDto(e))
                .collect(Collectors.toList());
        return transDtos;
    }

    @Override
    public AmountTransferDto getTransactionDetailsById(Integer transactionId) {
        Transaction transaction=this.transactionsRepo.findById(transactionId).orElseThrow(()->new ResourceNotFoundException("transaction","Id",transactionId));
        return this.transactionConverter.transferModelToDto(transaction);
    }

    @Override
    public TransactionDto deposit(Integer accountNumber, Double depositAmount) {
         TransactionDto trans=new TransactionDto();
        Account account=accountRepo.findById(accountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",accountNumber));
            trans.setToAccountNumber(accountNumber);
            trans.setTransferAmount(depositAmount);
            trans.setStatus("amount deposited");
            trans.setDate(LocalDate.now());
            trans.setTime(LocalTime.now());
            Transaction trans1=this.transactionConverter.transactionDtoToModel(trans);
            trans1.setFromAccountNumber(0);
            this.transactionsRepo.save(trans1);
            account.setTotalAmount(account.getTotalAmount() + depositAmount);
            this.accountRepo.save(account);
            return trans;
    }

    @Override
    public TransactionDto withDraw(Integer accountNumber, Double withdrawAmount) {
        Account account=accountRepo.findById(accountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",accountNumber));
        TransactionDto trans= new TransactionDto();
        if(account.getTotalAmount()>=withdrawAmount) {
            trans.setToAccountNumber(accountNumber);
            trans.setTransferAmount(withdrawAmount);
            trans.setStatus("amount withdrew");
            trans.setDate(LocalDate.now());
            trans.setTime(LocalTime.now());
            Transaction trans1=this.transactionConverter.transactionDtoToModel(trans);
            trans1.setFromAccountNumber(0);
            this.transactionsRepo.save(trans1);
            account.setTotalAmount(account.getTotalAmount() - withdrawAmount);
            this.accountRepo.save(account);
        }
        else {
            throw new InSufficientBalanceException(" Insufficient Amount.....");
        }
        return trans;
    }
    @Override
    public AmountTransferDto amountTransfer(Integer senderAccountNumber, Integer receiverAccountNumber, Double amount) {
        Account account=accountRepo.findById(senderAccountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",senderAccountNumber));
        Account account1=accountRepo.findById(receiverAccountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",receiverAccountNumber));
        AmountTransferDto trans=new AmountTransferDto();
        if(account.getTotalAmount()>=amount) {
            trans.setFromAccountNumber(senderAccountNumber);
            trans.setTransferAmount(amount);
            trans.setDate(LocalDate.now());
            trans.setTime(LocalTime.now());
            trans.setStatus(amount+" Rs withdrew from account "+senderAccountNumber+" and deposited in account "+receiverAccountNumber );
            Transaction trans1=this.transactionConverter.transferDtoToModel(trans);
            account.setTotalAmount(account.getTotalAmount() - amount);
            this.accountRepo.save(account);
        }
        else {
            throw new InSufficientBalanceException(" Insufficient Amount.....");
        }
        trans.setToAccountNumber(receiverAccountNumber);
        Transaction trans1=this.transactionConverter.transferDtoToModel(trans);
        this.transactionsRepo.save(trans1);
        account1.setTotalAmount(account1.getTotalAmount() + amount);
        this.accountRepo.save(account1);
        return trans;

    }
}
