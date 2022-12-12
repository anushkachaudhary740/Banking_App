package com.cognologix.BankSystemApplicationAssignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TransactionDto {
    private Integer toAccountNumber;
    private Double transferAmount;
    private String status;
    private LocalDate date;
    private LocalTime time;
}
