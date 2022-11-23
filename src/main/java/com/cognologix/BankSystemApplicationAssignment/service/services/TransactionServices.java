package com.cognologix.BankSystemApplicationAssignment.service.services;

import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionServices {
    TransactionDto createTransaction(TransactionDto transactionDto);
    List<TransactionDto> getTransactionDetails();
    void getDeposit(Integer transactionId, Double depositAmount);
    void getWithDraw(Integer transactionId, Double withdraw);
    Double getTotalBalance(Integer transactionId);
    void amountTransfer(Integer senderId, Integer receiverId, Double amount);
}
