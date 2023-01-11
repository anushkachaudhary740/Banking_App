package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.AccountErrors;
import com.cognologix.BankSystemApplicationAssignment.enums.transactionEnums.TransactionEnum;
import com.cognologix.BankSystemApplicationAssignment.exceptions.globalException.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import com.cognologix.BankSystemApplicationAssignment.responses.AllTransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Log4j2
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
    void getAllTransactionDetailsForOneAccount() {
        Integer accountNumber=1;
        List<Transaction> listOfTransaction = new ArrayList<>();
        listOfTransaction.add(Transaction.builder().toAccountNumber(1).fromAccountNumber(0).transferAmount(567.00).build());
        when(transactionsRepo.existsById(accountNumber)).thenReturn(true);
        when(transactionsRepo.findByToAccountNumber(accountNumber)).thenReturn(listOfTransaction);
        AllTransactionsResponse allTransactionsResponse = transactionServices.getAllTransactionDetailsForOneAccount(accountNumber);
        assertEquals(TransactionEnum.ALL_TRANSACTIONS.getMessage(), allTransactionsResponse.getMessage());
    }

    @Test
    void getTransactionDetailsForOneAccountWithNegativeScenarioIfAccountNotExist() {
        Integer accountNumber=89;
        List<Transaction> listOfTransaction = new ArrayList<>();
        when(transactionsRepo.existsById(accountNumber)).thenReturn(false);
        when(transactionsRepo.findByToAccountNumber(accountNumber)).thenReturn(listOfTransaction);
        AllTransactionsResponse allTransactionsResponse = transactionServices.getAllTransactionDetailsForOneAccount(accountNumber);
        assertEquals(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), allTransactionsResponse.getMessage());
    }

    @Test
    void getTransactionDetailsById() {
        Integer transactionId=1;
        Transaction transaction = converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.existsById(1)).thenReturn(true);
        when(transactionsRepo.findById(1)).thenReturn(Optional.ofNullable(transaction));
        Optional<TransactionDto> trans = transactionServices.getTransactionDetailsById(1);
        assertEquals(1, transactionId);
    }

    @Test
    void getTransactionDetailsByIdWithNegativeScenarioIfIdNotExist() {
        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.findById(1)).thenReturn(Optional.of(transaction));
        assertThrows(ResourceNotFoundException.class, () -> transactionServices.getTransactionDetailsById(12));
    }

    @Test
    void depositAmount() {
        account.setAccountStatus("ACTIVATED");
        account.setAccountNumber(1);
        account.setTotalAmount(0.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());

        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.depositAmount(1, 500.0);
        assertEquals("Rs 500.0"  +TransactionEnum.DEPOSIT_AMOUNT.getMessage(), transactionsResponse1.getMessage());
    }

    @Test
    void depositAmountWithNegativeScenarioIfAccountNotExist() {
        account.setAccountStatus("DEACTIVATED");
        account.setAccountNumber(1);
        account.setTotalAmount(0.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(false);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());

        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.depositAmount(1, 500.0);
        assertEquals(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), transactionsResponse1.getMessage());

    }

    @Test
    void withDrawAmount() {
        account.setAccountStatus("ACTIVATED");
        account.setAccountNumber(1);
        account.setTotalAmount(100.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.withdrawAmount(1, 20.0);
        assertEquals("Rs 20.0"+TransactionEnum.WITHDRAW_AMOUNT.getMessage(), transactionsResponse1.getMessage());

    }

    @Test
    void withdrawAmountWithNegativeScenarioIfAccountNotExist() {
        account.setAccountStatus("DEACTIVATED");
        account.setAccountNumber(1);
        account.setTotalAmount(100.0);
        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(false);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        TransactionsResponse transactionsResponse1 = transactionServices.withdrawAmount(1, 20.0);
        assertEquals(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), transactionsResponse1.getMessage());

    }

    @Test
    void transferAmount() {
        account1 = Account.builder()
                .accountNumber(2)
                .totalAmount(500.0)
                .accountStatus("ACTIVATED")
                .build();
        account.setAccountStatus("ACTIVATED");
        Optional<Account> prevAccount = Optional.of(account);
        Optional<Account> prevAccount1 = Optional.of(account1);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.existsById(account1.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.findById(account1.getAccountNumber())).thenReturn(prevAccount1);

        Transaction transaction = this.converter.transactionDtoToModel(transactionDto);
        when(transactionsRepo.save(transaction)).thenReturn(transaction);
        TransactionsResponse transactionsResponse1 = transactionServices.transferAmount(1, 2, 40.0);
        assertEquals("Rs 40.0"+TransactionEnum.TRANSFER.getMessage(), transactionsResponse1.getMessage());
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
        assertEquals(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), transactionsResponse1.getMessage());

    }
}