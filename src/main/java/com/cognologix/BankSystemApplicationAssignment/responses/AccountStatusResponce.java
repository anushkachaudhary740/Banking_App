package com.cognologix.BankSystemApplicationAssignment.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountStatusResponce {
    private String message;
    private Boolean success;
}
