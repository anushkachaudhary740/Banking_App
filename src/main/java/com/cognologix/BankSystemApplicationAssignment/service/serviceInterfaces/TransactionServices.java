package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.dto.AmountTransferDto;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TransactionServices {
    List<AmountTransferDto> getTransactionDetails();
    AmountTransferDto getTransactionDetailsById(Integer transactionId);
    TransactionDto deposit(Integer accountNumber, Double depositAmount);
    TransactionDto withDraw(Integer accountNumber, Double withdraw);
   AmountTransferDto amountTransfer(Integer senderId, Integer receiverId, Double amount);
}
