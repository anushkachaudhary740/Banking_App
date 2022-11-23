package com.cognologix.BankSystemApplicationAssignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {
    private Integer customerId;
    private String customerName;
    private String customerMobileNumber;
    private String customerEmail;
    private String customerPanCardNumber;
    private String customerAadharCardNumber;
    private String customerDateOfBirth;
    private List<AccountDto> accountDto;
}
