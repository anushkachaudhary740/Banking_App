package com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums;

import lombok.Getter;

@Getter
public enum AccountErrors {
    ACCOUNT_ALREADY_ACTIVATE(800, "Account already activated"),

    ACCOUNT_ALREADY_DEACTIVATE(801, "Already deactivated"),

    ACCOUNT_ALREADY_EXIST(802, "Account already exist"),

    ACCOUNT_NOT_AVAILABLE(803, "Account not available"),
    ACCOUNT_NOT_FOUND(808, "Account not found"),

    DEACTIVATE_ACCOUNT(804, "Account deactivated"),

    NO_ANY_DEACTIVATED_ACCOUNT_FOUND(808, "No any deactivated account found"),

    ILLEGAL_TYPE_OF_ACCOUNT(805, "Invalid account type"),
    ILLEGALE_TYPE_OF_ACCOUNT_STATUS(806,"Invalid account status"),
    INACTIVE_AMOUNT_RECEIVER_ACCOUNT(807, "inactive receiver account"),
    ACCOUNT_ALREADY_ACTIVATED(808,"Account is already activated"),
    INVALID_ACCOUNT_NUMBER(809,"Invalid account number");

    private final Integer code;
    private final String message;

    AccountErrors(Integer code, String message) {
        this.code = code;
        this.message = message;

    }
}
