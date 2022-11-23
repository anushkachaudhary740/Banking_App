package com.cognologix.BankSystemApplicationAssignment.model;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    @Id
    @Column(name="account_number",nullable = false)
    private Integer accountNumber;
    private String bankName;
    private String typeOfAccount;
    private Double totalAmount;
//    @ManyToOne(cascade = CascadeType.ALL)
//    private Customer customer;
}
