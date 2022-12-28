package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServicesTest {
    @Autowired
    private TransactionServices transactionServices;

    private TransactionDto transactionDto;
    @MockBean
    private TransactionsRepo transactionsRepo;
    @Autowired
    private Converter converter;
    @MockBean
    private AccountRepo accountRepo;
    private Account account;
    private Account account1;
    @BeforeEach
    void setUp() {
        transactionDto = TransactionDto.builder()
                .transactionId(1)
                .toAccountNumber(1)
                .fromAccountNumber(2)
                .transferAmount(0.00)
                .status("testing")
                .build();
        account = Account.builder()
                .accountNumber(1)
                .bankName("SBI Bank")
                .typeOfAccount("saving")
                .totalAmount(500.00)
                .build();
    }

    @Test
    void getTransactionDetails() {
        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.findAll()).thenReturn(List.of(transaction));
        List<TransactionDto> transactionList = transactionServices.getTransactionDetails();
        assertEquals(1, transactionList.size());
    }

    @Test
    void getTransactionDetailsWithNegativeScenarioIfDetailsNotFound() {
        when(transactionsRepo.findAll()).thenReturn(List.of());
        List<TransactionDto> transactionList = transactionServices.getTransactionDetails();
        assertEquals(0, transactionList.size());

    }

    @Test
    void getTransactionDetailsById() {
        Transaction transaction = converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.existsById(1)).thenReturn(true);
        when(transactionsRepo.findById(1)).thenReturn(Optional.ofNullable(transaction));
        Optional<TransactionDto> trans = transactionServices.getTransactionDetailsById(1);
        assertEquals(1, trans.get().getTransactionId());
    }

    @Test
    void getTransactionDetailsByIdWithNegativeScenarioIfIdNotExist() {
        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.findById(1)).thenReturn(Optional.of(transaction));
        assertThrows(ResourceNotFoundException.class, () -> transactionServices.getTransactionDetailsById(12));
    }

    @Test
    void depositAmount() {
        account.setAccountNumber(1);
        account.setTotalAmount(0.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());

        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.depositAmount(1, 500.0);
        assertEquals("Rs 500.0 successfully deposit.....", transactionsResponse1.getMessage());
    }

    @Test
    void depositAmountWithNegativeScenarioIfAccountNotExist() {
        account.setAccountNumber(1);
        account.setTotalAmount(0.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(false);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());

        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.depositAmount(1, 500.0);
        assertEquals("Account does not exist", transactionsResponse1.getMessage());

    }

    @Test
    void withDrawAmount() {
        account.setAccountNumber(1);
        account.setTotalAmount(100.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.withdrawAmount(1, 20.0);
        assertEquals("Rs 20.0 successfully withdraw.....", transactionsResponse1.getMessage());

    }

    @Test
    void withdrawAmountWithNegativeScenarioIfAccountNotExist() {
        account.setAccountNumber(1);
        account.setTotalAmount(100.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(false);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        TransactionsResponse transactionsResponse1 = transactionServices.withdrawAmount(1, 20.0);
        assertEquals("Account does not exist", transactionsResponse1.getMessage());

    }

    @Test
    void transferAmount() {
        account1 = Account.builder()
                .accountNumber(2)
                .totalAmount(500.0)
                .build();
        Optional<Account> prevAccount = Optional.of(account);
        Optional<Account> prevAccount1 = Optional.of(account1);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.existsById(account1.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.findById(account1.getAccountNumber())).thenReturn(prevAccount1);

        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.transferAmount(1, 2, 40.0);
        assertEquals("Rs 40.0 successfully transfer.....", transactionsResponse1.getMessage());
    }

    @Test
    void transferAmountWithNegativeScenarioIfAccountNotExist() {
        account1 = Account.builder()
                .accountNumber(2)
                .totalAmount(500.0)
                .build();
        Optional<Account> prevAccount = Optional.of(account);
        Optional<Account> prevAccount1 = Optional.of(account1);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(false);
        when(accountRepo.existsById(account1.getAccountNumber())).thenReturn(false);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.findById(account1.getAccountNumber())).thenReturn(prevAccount1);

        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.transferAmount(1, 2, 40.0);
        assertEquals("Account does not exist", transactionsResponse1.getMessage());

    }
}