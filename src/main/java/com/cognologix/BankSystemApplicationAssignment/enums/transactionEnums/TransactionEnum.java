package com.cognologix.BankSystemApplicationAssignment.enums.transactionEnums;

import lombok.Getter;

@Getter
public enum TransactionEnum {
    DEPOSIT_AMOUNT(" deposited successfully"),
    WITHDRAW_AMOUNT(" withdraw successfully"),
    PRINT_ALL_TRANSACTION("All transaction details are printed successfully"),
    ALL_TRANSACTIONS("all transactions for this account is..."),
    TRANSFER("successfully transfer....."),
    TRANSFER_AMOUNT(" transferred successfully Remaining balance : ");
    private String message;
    TransactionEnum(String message)
    {
        this.message=message;
    }

}
