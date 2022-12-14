package com.cognologix.BankSystemApplicationAssignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransactionDto {
    private Integer transactionId;
    private Integer toAccountNumber;
    private Integer fromAccountNumber;
    private Double transferAmount;
    private String status;
    private LocalDate date;
    private LocalTime time;
}
