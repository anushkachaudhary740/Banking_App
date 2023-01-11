package com.cognologix.BankSystemApplicationAssignment.service.serviceImplementation;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.AccountRepo;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountMsgEnums;
import com.cognologix.BankSystemApplicationAssignment.enums.accountEnums.AccountStatus;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.AccountErrors;
import com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums.TransactionErrors;
import com.cognologix.BankSystemApplicationAssignment.enums.transactionEnums.TransactionEnum;
import com.cognologix.BankSystemApplicationAssignment.exceptions.accountException.DeactivateAccountException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.InSufficientBalanceException;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.TransactionsNotAvailableException;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import com.cognologix.BankSystemApplicationAssignment.responses.AllTransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.exceptions.globalException.ResourceNotFoundException;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@Log4j2
public class TransactionServicesImplementation implements TransactionServices {
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private Converter transactionConverter;
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public AllTransactionsResponse getAllTransactionDetailsForOneAccount(Integer accountNumber) {
        if (transactionsRepo.existsById(accountNumber)) {
            List<Transaction> transaction = transactionsRepo.findByToAccountNumber(accountNumber);
            if (transaction.isEmpty()) {
                log.error(TransactionErrors.TRANSACTIONS_NOT_AVAILABLE.getMessage() +accountNumber);
                throw new TransactionsNotAvailableException("Transactions", "Id", accountNumber);
            }
            AllTransactionsResponse transactionsResponse = new AllTransactionsResponse(TransactionEnum.ALL_TRANSACTIONS.getMessage(), true, transaction);
            log.info(transactionsResponse.getMessage());
            return transactionsResponse;
        } else {
            log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage());
            return new AllTransactionsResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), false, null);
        }
    }

    @Override
    public Optional<TransactionDto> getTransactionDetailsById(Integer transactionId) {
        if (transactionsRepo.existsById(transactionId)) {
            Transaction transaction = this.transactionsRepo.findById(transactionId).get();
            log.info(TransactionEnum.PRINT_ALL_TRANSACTION.getMessage());
            return Optional.ofNullable(this.transactionConverter.transferModelToDto(transaction));
        } else {
            log.error(TransactionErrors.TRANSACTION_NOT_FOUND.getMessage()+transactionId);
            throw new ResourceNotFoundException("transaction", "Id", transactionId);
        }
    }

    @Override
    public TransactionsResponse depositAmount(Integer accountNumber, Double depositAmount) {
        TransactionDto trans = new TransactionDto();
        if (accountRepo.existsById(accountNumber)) {
            Account account = accountRepo.findById(accountNumber).get();
            log.info("status" + account.getAccountStatus());
            if (account.getAccountStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                log.error(AccountMsgEnums.ACCOUNT_IS_DEACTIVATE.getMessage() +accountNumber);
                throw new DeactivateAccountException("Account", "Number", accountNumber);
            }
            trans.setToAccountNumber(accountNumber);
            trans.setTransferAmount(depositAmount);
            trans.setStatus("amount deposited");
            trans.setDate(LocalDate.now());
            trans.setTime(LocalTime.now());
            trans.setFromAccountNumber(0);
            Transaction trans1 = this.transactionConverter.transactionDtoToModel(trans);
            trans1.setFromAccountNumber(0);
            this.transactionsRepo.save(trans1);
            account.setTotalAmount(account.getTotalAmount() + depositAmount);
            this.accountRepo.save(account);
            TransactionsResponse transactionsResponse = new TransactionsResponse("Rs " + depositAmount + TransactionEnum.DEPOSIT_AMOUNT.getMessage(), true, trans);
            log.info(transactionsResponse.getMessage());
            return transactionsResponse;
        } else {
            TransactionsResponse transactionsResponse = new TransactionsResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), false, null);
            log.error(transactionsResponse.getMessage());
            return transactionsResponse;
        }
    }

    @Override
    public TransactionsResponse withdrawAmount(Integer accountNumber, Double withdrawAmount) {
        if (accountRepo.existsById(accountNumber)) {
            Account account = accountRepo.findById(accountNumber).get();
            if (account.getAccountStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                log.error(AccountMsgEnums.ACCOUNT_IS_DEACTIVATE.getMessage(),accountNumber);
                throw new DeactivateAccountException("Account", "Number", accountNumber);
            }
            TransactionDto trans = new TransactionDto();
            if (account.getTotalAmount() >= withdrawAmount) {
                trans.setToAccountNumber(accountNumber);
                trans.setTransferAmount(withdrawAmount);
                trans.setStatus("amount withdrew");
                trans.setDate(LocalDate.now());
                trans.setTime(LocalTime.now());
                trans.setFromAccountNumber(0);
                Transaction trans1 = this.transactionConverter.transactionDtoToModel(trans);
                trans1.setFromAccountNumber(0);
                this.transactionsRepo.save(trans1);
                account.setTotalAmount(account.getTotalAmount() - withdrawAmount);
                this.accountRepo.save(account);
                TransactionsResponse transactionsResponse = new TransactionsResponse("Rs " + withdrawAmount + TransactionEnum.WITHDRAW_AMOUNT.getMessage(), true, trans);
                log.info(transactionsResponse.getMessage());
                return transactionsResponse;
            } else {
                log.error(TransactionErrors.INSUFFICIENT_AMOUNT.getMessage());
                throw new InSufficientBalanceException(TransactionErrors.INSUFFICIENT_AMOUNT.getMessage());
            }
        } else {
            log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage());
            return new TransactionsResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), false, null);
        }
    }

    @Override
    public TransactionsResponse transferAmount(Integer senderAccountNumber, Integer receiverAccountNumber, Double amount) {
        if (accountRepo.existsById(senderAccountNumber) && accountRepo.existsById(receiverAccountNumber)) {
            Account account = accountRepo.findById(senderAccountNumber).get();
            Account depositAccount = accountRepo.findById(receiverAccountNumber).get();
            if (account.getAccountStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                log.error(AccountMsgEnums.ACCOUNT_IS_DEACTIVATE.getMessage()+senderAccountNumber);
                throw new DeactivateAccountException("Account", "Number", senderAccountNumber);
            }
            if (depositAccount.getAccountStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                log.error(AccountMsgEnums.ACCOUNT_IS_DEACTIVATE.getMessage()+receiverAccountNumber);
                throw new DeactivateAccountException("Account", "Number", receiverAccountNumber);
            }
            TransactionDto trans = new TransactionDto();
            if (account.getTotalAmount() >= amount) {
                trans.setFromAccountNumber(senderAccountNumber);
                trans.setTransferAmount(amount);
                trans.setDate(LocalDate.now());
                trans.setTime(LocalTime.now());
                trans.setStatus("Rs "+amount + " withdraw from account " + senderAccountNumber + " and deposited in account " + receiverAccountNumber);
                Transaction trans1 = this.transactionConverter.transferDtoToModel(trans);
                account.setTotalAmount(account.getTotalAmount() - amount);
                this.accountRepo.save(account);
            } else {
                log.error(TransactionErrors.INSUFFICIENT_AMOUNT.getMessage());
                throw new InSufficientBalanceException(TransactionErrors.INSUFFICIENT_AMOUNT.getMessage());
            }
            trans.setToAccountNumber(receiverAccountNumber);
            Transaction trans1 = this.transactionConverter.transferDtoToModel(trans);
            this.transactionsRepo.save(trans1);
            depositAccount.setTotalAmount(depositAccount.getTotalAmount() + amount);
            this.accountRepo.save(depositAccount);
            TransactionsResponse transactionsResponse = new TransactionsResponse("Rs " + amount + TransactionEnum.TRANSFER.getMessage(), true, trans);
            log.error(transactionsResponse.getMessage());
            return transactionsResponse;
        } else {
            log.error(AccountErrors.ACCOUNT_NOT_FOUND.getMessage());
            return new TransactionsResponse(AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), false, null);
        }

    }
}
