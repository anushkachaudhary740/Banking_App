package com.cognologix.BankSystemApplicationAssignment.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponse {
    private String message;
    private Boolean success;

}
