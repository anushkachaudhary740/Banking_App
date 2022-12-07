package com.cognologix.BankSystemApplicationAssignment.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerDto {
    private Integer customerId;
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
    private String address;
    private String pinCode;
    private List<AccountDto> accountDetails;

    public CustomerDto(Integer customerId, String customerName, String gender, String customerMobileNumber, String customerEmail, String customerPanCardNumber, String customerAadharCardNumber, String customerDateOfBirth) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.gender = gender;
        this.customerMobileNumber = customerMobileNumber;
        this.customerEmail = customerEmail;
        this.customerPanCardNumber = customerPanCardNumber;
        this.customerAadharCardNumber = customerAadharCardNumber;
        this.customerDateOfBirth = customerDateOfBirth;
    }
}
