package com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums;

import lombok.Getter;

@Getter
public enum CustomerErrors {
    CUSTOMER_ALREADY_EXIST(701,"customer already exist by aadhar number or pan number"),

    CUSTOMER_NOT_FOUND(702,"customer not found"),
    ACCOUNT_NOT_AVAILABLE_FOR_CUSTOMER(705,"Account not available for customer Id: "),

    DUPLICATE_CUSTOMER_ID(703,"Duplicate customer id"),

    ACCOUNT_NOT_AVAILABLE(704,"No account found for this customer");
    private final Integer code;
    private final String message;
    CustomerErrors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
