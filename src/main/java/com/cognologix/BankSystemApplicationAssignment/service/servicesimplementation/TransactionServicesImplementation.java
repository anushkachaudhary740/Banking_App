package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.TransactionConverter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.exceptions.InSufficientBalanceException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServicesImplementation implements TransactionServices {
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private TransactionConverter transactionConverter;
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public List<TransactionDto> getTransactionDetails() {
        List<Transaction> list = this.transactionsRepo.findAll();
        List<TransactionDto> transDtos = list.stream().map(e -> this.transactionConverter.transactionModelToDto(e))
                .collect(Collectors.toList());
        return transDtos;
    }

    @Override
    public TransactionDto getTransactionDetailsById(Integer transactionId) {
        Transaction transaction=this.transactionsRepo.findById(transactionId).orElseThrow(()->new ResourceNotFoundException("transaction","Id",transactionId));
        return this.transactionConverter.transactionModelToDto(transaction);
    }

    @Override
    public void getDeposit(Integer accountNumber, Double depositAmount) {
         TransactionDto trans=new TransactionDto();
        Account account=accountRepo.findById(accountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",accountNumber));
            trans.setToAccountNumber(accountNumber);
            //trans.setTotalBalance(account.getTotalAmount() + depositAmount);
            //trans.setWithdrawAmount(0.0);
            trans.setTransferAmount(depositAmount);
            trans.setFromAccountNumber(0);
            trans.setStatus("amount deposited");
            Transaction trans1=this.transactionConverter.transactionDtoToModel(trans);
            this.transactionsRepo.save(trans1);
            account.setTotalAmount(account.getTotalAmount() + depositAmount);
            this.accountRepo.save(account);
    }

    @Override
    public void getWithDraw(Integer accountNumber,Double withdrawAmount) {
        Account account=accountRepo.findById(accountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",accountNumber));
        if(account.getTotalAmount()>=withdrawAmount) {
            TransactionDto trans=new TransactionDto();
            trans.setToAccountNumber(accountNumber);
            trans.setFromAccountNumber(0);
            //trans.setTotalBalance(account.getTotalAmount() - withdrawAmount);
            //trans.setDepositAmount(0.0);
            trans.setTransferAmount(withdrawAmount);
            trans.setStatus("amount withdrew");
            Transaction trans1=this.transactionConverter.transactionDtoToModel(trans);
            this.transactionsRepo.save(trans1);
            account.setTotalAmount(account.getTotalAmount() - withdrawAmount);
            this.accountRepo.save(account);
        }
        else {
            throw new InSufficientBalanceException(" Insufficient Amount.....");
        }
    }
    @Override
    public void amountTransfer(Integer senderAccountNumber, Integer receiverAccountNumber, Double amount) {
        //withdraw
        Account account=accountRepo.findById(senderAccountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",senderAccountNumber));
        TransactionDto trans=new TransactionDto();
        if(account.getTotalAmount()>=amount) {
            //trans.setToAccountNumber(senderAccountId);
            trans.setFromAccountNumber(senderAccountNumber);
            //trans.setTotalBalance(account.getTotalAmount() - amount);
            //trans.setDepositAmount(0.0);
            trans.setTransferAmount(amount);
            trans.setStatus(amount+" Rs withdrew from account "+senderAccountNumber+" and deposited in account "+receiverAccountNumber );
            Transaction trans1=this.transactionConverter.transactionDtoToModel(trans);
            //this.transactionsRepo.save(trans1);
            account.setTotalAmount(account.getTotalAmount() - amount);
            this.accountRepo.save(account);
        }
        else {
            throw new InSufficientBalanceException(" Insufficient Amount.....");
        }

        Account account1=accountRepo.findById(receiverAccountNumber)
                .orElseThrow(()->new ResourceNotFoundException("account","number",receiverAccountNumber));
        trans.setToAccountNumber(receiverAccountNumber);
        //trans.setTotalBalance(account1.getTotalAmount() + amount);
        //trans.setWithdrawAmount(0.0);
        //trans.setDepositAmount(amount);
        //trans.setFromAccountNumber(0);
        //trans.setMessage("amount deposited");
        Transaction trans1=this.transactionConverter.transactionDtoToModel(trans);
        this.transactionsRepo.save(trans1);
        account1.setTotalAmount(account1.getTotalAmount() + amount);
        this.accountRepo.save(account1);

    }
}
