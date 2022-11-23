package com.cognologix.BankSystemApplicationAssignment.dto;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDto {
    private Integer transactionId;
    private Double Balance;
    private Double depositAmount;
    private Double withdrawAmount;
}
