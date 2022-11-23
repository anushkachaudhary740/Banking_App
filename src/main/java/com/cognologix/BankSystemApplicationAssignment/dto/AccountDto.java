package com.cognologix.BankSystemApplicationAssignment.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDto {
    private Integer accountNumber;
    private String bankName;
    private String typeOfAccount;
    private Double totalAmount;
   // private CustomerDto customerDto;

}
