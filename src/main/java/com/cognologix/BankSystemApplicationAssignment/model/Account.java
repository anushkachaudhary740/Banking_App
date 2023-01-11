package com.cognologix.BankSystemApplicationAssignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountNumber;
    private String accountHolderName;
    private String  accountStatus;
    private String bankName;
    private String typeOfAccount;
    private Double totalAmount;
    @OneToOne
    private Customer customer;
}
