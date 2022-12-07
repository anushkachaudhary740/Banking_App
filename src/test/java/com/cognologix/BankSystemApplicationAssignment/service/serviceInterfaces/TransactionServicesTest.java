package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.TransactionConverter;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServicesTest {
    @Autowired
    private TransactionServices transactionServices;

    private TransactionDto transactionDto;
    @MockBean
    private TransactionsRepo transactionsRepo;
    @Autowired
    private TransactionConverter transactionConverter;
    @BeforeEach
    void setUp(){
        transactionDto = TransactionDto.builder()
                .transactionId(21)
                .toAccountNumber(1)
                .fromAccountNumber(2)
                .transferAmount(100.00)
                .build();
    }
    @Test
    void getTransactionDetails() {
        Transaction transaction=this.transactionConverter.transactionDtoToModel(transactionDto);
        transactionsRepo.save(transaction);
        transactionServices.getTransactionDetails();
        verify(transactionsRepo).findAll();
//        when(transactionsRepo.findAll()).thenReturn((List<Transaction>) Stream
//                .of(new Transaction( 3234,1,2,700.0,"Testing")).collect(Collectors.toList()));
//        assertEquals(1, transactionServices.getTransactionDetails().size());

    }

    @Test
    void getTransactionDetailsById() {
    }

    @Test
    void getDeposit() {
    }

    @Test
    void getWithDraw() {
    }

    @Test
    void amountTransfer() {
        Double amount =100.00;
        Transaction accMoneyTransfer = mock(Transaction.class);
        TransactionDto accMoneyTransferSaveDto = mock(TransactionDto.class);

        when(accMoneyTransfer.getTransferAmount()).thenReturn(100.00);

        transactionServices.amountTransfer(accMoneyTransfer.getToAccountNumber(),accMoneyTransfer.getFromAccountNumber(),amount);

        verify(transactionsRepo.save(accMoneyTransfer));

    }
}