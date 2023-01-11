package com.cognologix.BankSystemApplicationAssignment.enums.errorCodeEnums;

import lombok.Getter;

@Getter
public enum GlobalErrors {
    RESOURCE_NOT_FOUND(600,"Resource Not Found"),
    EMPTY_LIST(601,"List Is Empty");
    private final Integer code;
    private final String message;

    GlobalErrors(Integer code, String message) {
        this.code = code;
        this.message = message;

    }

}
