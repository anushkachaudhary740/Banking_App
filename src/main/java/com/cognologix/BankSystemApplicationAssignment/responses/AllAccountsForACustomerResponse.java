package com.cognologix.BankSystemApplicationAssignment.responses;

import com.cognologix.BankSystemApplicationAssignment.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAccountsForACustomerResponse {
        private String message;
        private Boolean success;
        private List<Account> account;



}
