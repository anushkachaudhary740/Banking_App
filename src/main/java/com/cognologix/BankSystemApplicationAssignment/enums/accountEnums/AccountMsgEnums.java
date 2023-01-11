package com.cognologix.BankSystemApplicationAssignment.enums.accountEnums;

import lombok.Getter;

@Getter
public enum AccountMsgEnums {
    PRINT_ACCOUNT("All account details are ptinted successfully"),
    PRINT_ACCOUNT_DETAILS_FOR_ONE_ACCOUNT("Account details are printed for one specific accountNumber"),
    CREATE_ACCOUNT("Account created successfully"),
    AVAILABLE_BALANCE("Total balance is : "),
    UPDATE_ACCOUNT("Account updated successfully"),
    BALANCE_INFO("Your account Balance is not zero "),

    DEACTIVATE_ACCOUNT("Account successfully deactivated "),

    ACTIVATED_ACCOUNT("Your Account is Activated Successfully "),
    DELETE_ACCOUNT("Account deleted successfully.."),
    ACCOUNT_IS_DEACTIVATE("Account is DEACTIVATED with number: "),

    LIST_OF_DEACTIVATED_ACCOUNTS("Deactivated accounts are");

    private String message;
    AccountMsgEnums(String message)
    {
        this.message=message;
    }


}
