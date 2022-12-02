package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TransactionServices {
    List<TransactionDto> getTransactionDetails();
    TransactionDto getTransactionDetailsById(Integer transactionId);
    void getDeposit(Integer accountNumber, Double depositAmount);
    void getWithDraw(Integer accountNumber, Double withdraw);
   void amountTransfer(Integer senderId, Integer receiverId, Double amount);
}
