package com.cognologix.BankSystemApplicationAssignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDto {
    private Integer transactionId;
    //private Integer accountNumber;
    private Integer toAccountNumber;
    private Integer fromAccountNumber;
    private Double transferAmount;
    private String status;
}
