package com.cognologix.BankSystemApplicationAssignment.enums.customerEnums;

import lombok.Getter;

@Getter
public enum CustomerMsgEnum {
    CREATE_CUSTOMER("Customer created successfully"),
    STATEMENT("Successfully get bank statement"),
    ALL_ACCOUNTS_FOR_CUSTOMER("All accounts for this customer..."),
    UPDATE_CUSTOMER("Customer details updated successfully"),
    PRINT_CUSTOMER("Customer details for a specific Id is printed successfully"),
    CUSTOMER_DETAILS("All customer details are printed successfully"),
    DELETE_CUSTOMER("Customer details deleted successfully..");


    private String message;
    CustomerMsgEnum(String message)
    {
        this.message=message;
    }
}

