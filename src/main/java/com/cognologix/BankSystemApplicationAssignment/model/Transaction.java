package com.cognologix.BankSystemApplicationAssignment.model;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Transaction {
    @Id
    @NotNull
    private Integer transactionId;
    private Double balance;
    private Double depositAmount;
    private Double withdrawAmount;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedDate = new Date();
}
