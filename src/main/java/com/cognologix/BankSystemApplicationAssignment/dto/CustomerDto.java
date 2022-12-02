package com.cognologix.BankSystemApplicationAssignment.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {
    @NotNull
    private String customerName;
//    @NotNull
//    private String typeOfAccount;
//    @NotNull
//    private String bankName;
    @NotNull
    private String gender;
    @NotNull
    private String customerMobileNumber;
    @NotNull
    private String customerEmail;
    @NotNull
    private String customerPanCardNumber;
    @NotNull
    private String customerAadharCardNumber;
    @NotNull
    private String customerDateOfBirth;
    //private List<AccountDto> accountDto;
}
