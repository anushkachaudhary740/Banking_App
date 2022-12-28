package com.cognologix.BankSystemApplicationAssignment.responses;

import com.cognologix.BankSystemApplicationAssignment.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
private String message;
private Boolean success;
private CustomerDto customer;
}
