package com.cognologix.BankSystemApplicationAssignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transactionId;
    private Integer toAccountNumber;
    private Integer fromAccountNumber;
    private Double transferAmount;
    private String status;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date DateAndTime = new Date();
}
