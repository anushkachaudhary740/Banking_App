package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface TransactionServices {
    List<TransactionDto> getTransactionDetails();
    Optional<TransactionDto> getTransactionDetailsById(Integer transactionId);
    TransactionsResponse depositAmount(Integer accountNumber, Double depositAmount);
    TransactionsResponse withdrawAmount(Integer accountNumber, Double withdraw);
   TransactionsResponse transferAmount(Integer senderId, Integer receiverId, Double amount);
}
