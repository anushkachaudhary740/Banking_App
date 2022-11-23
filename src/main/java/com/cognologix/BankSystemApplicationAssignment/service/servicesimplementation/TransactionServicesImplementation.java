package com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation;
import com.cognologix.BankSystemApplicationAssignment.converter.transactionconverter.TransactionDtoToModelConverter;
import com.cognologix.BankSystemApplicationAssignment.converter.transactionconverter.TransactionModelToDtoConverter;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.service.services.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServicesImplementation implements TransactionServices {
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private TransactionDtoToModelConverter transactionDtoToModelConverter;
    @Autowired
    private TransactionModelToDtoConverter transactionModelToDtoConverter;

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction transactionDetails = this.transactionDtoToModelConverter.transactionDtoToModel(transactionDto);
        Transaction trans = this.transactionsRepo.save(transactionDetails);
        return this.transactionModelToDtoConverter.transactionModelToDto(trans);
    }
    @Override
    public List<TransactionDto> getTransactionDetails() {
        List<Transaction> list=this.transactionsRepo.findAll();
        List<TransactionDto> transDtos =list.stream().map(e-> this.transactionModelToDtoConverter.transactionModelToDto(e))
                .collect(Collectors.toList());
        return transDtos;
    }

    @Override
    public void getDeposit(Integer transactionId, Double depositAmount) {
        Transaction trans= transactionsRepo.findById(transactionId)
                .orElseThrow(()->new ResourceNotFoundException("account","number", transactionId));
        System.out.println(trans);
        Double totalBalance = trans.getBalance() + depositAmount;
        trans.setBalance(totalBalance);
        trans.setDepositAmount(depositAmount);
        this.transactionsRepo.save(trans);
        System.out.println("total Balance :"+trans.getBalance());

    }

    @Override
    public void getWithDraw(Integer transactionId, Double withdrawAmount) {
        Transaction list1 = transactionsRepo.findById(transactionId).orElseThrow(()->new ResourceNotFoundException("Transaction","Id",transactionId));
        Double totalBalance = list1.getBalance() - withdrawAmount;
        list1.setBalance(totalBalance);
        list1.setWithdrawAmount(withdrawAmount);
        this.transactionsRepo.save(list1);
        System.out.println("total Balance after withdraw:"+list1.getBalance());
    }
    @Override
    public Double getTotalBalance(Integer transactionId) {
        Optional<Transaction> list1 = transactionsRepo.findById(transactionId);
                Transaction Balance1 = list1.get();
        Double totalBalance = Balance1.getBalance();
        this.transactionModelToDtoConverter.transactionModelToDto(Balance1);
        return totalBalance;
    }

    @Override
    public void amountTransfer(Integer senderId, Integer receiverId, Double amount) {
        getDeposit(receiverId,amount);
         getWithDraw(senderId,amount);
    }
}
