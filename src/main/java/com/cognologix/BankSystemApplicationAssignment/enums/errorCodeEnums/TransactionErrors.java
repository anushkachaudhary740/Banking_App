package com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums;
import com.cognologix.BankSystemApplicationAssignment.exceptions.transactionException.BalanceException;
import lombok.Getter;

@Getter
public enum TransactionErrors {
    INSUFFICIENT_BALANCE(900, "Insufficient balance"),
    BALANCE_EXCEPTION(901,"Your account Balance is not zero"),
    TRANSACTION_NOT_FOUND(903,"Transaction not fount with Id: "),
    INSUFFICIENT_AMOUNT(904,"Insufficient Amount....."),
    TRANSACTIONS_NOT_AVAILABLE(902,"No any transactions available for account Id: ");
    private final Integer code;
    private final String message;

    TransactionErrors(Integer code, String message) {
        this.code = code;
        this.message = message;

    }

}
