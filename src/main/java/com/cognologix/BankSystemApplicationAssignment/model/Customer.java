package com.cognologix.BankSystemApplicationAssignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
    public class Customer {
        @Id
        //@GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer customerId;

        @Column(name="name",nullable = false,length = 100)
        private String customerName;
        private String customerGender;
        //private String bankName;
        @Column(name="mobile_number")
        private String customerMobileNumber;

        @Column(name="email")
        private String customerEmail;

        @Column(name="panCard_number")
        private String customerPanCardNumber;

        @Column(name="aadhar_card_number")
        private String customerAadharCardNumber;

        @Column(name="date_of_birth")
        private String customerDateOfBirth;

//        @OneToMany(targetEntity = Account.class,cascade = CascadeType.ALL)
//        @JoinColumn(name = "customerId",referencedColumnName = "customerId")
//        private List<Account> account;

        @Column(updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private Date CreatedDate = new Date();


}
