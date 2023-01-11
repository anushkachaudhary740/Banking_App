package com.cognologix.BankSystemApplicationAssignment.responses;

import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllTransactionsResponse {
    private String message;
    private Boolean success;
    private List<Transaction> transaction;

}
