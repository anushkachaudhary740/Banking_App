package com.cognologix.BankSystemApplicationAssignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@ToString
    public class Customer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer customerId;

        @Column(name="name",nullable = false,length = 100)
        private String customerName;

        private String customerGender;

        @Column(name="mobile_number",length = 10)
        @NotBlank(message = "Provide Valid Mobile Number")
        private String customerMobileNumber;

        @Column(name="email")
        @NotBlank(message = "Provide Valid Email Id")
        private String customerEmail;

        @NotBlank(message = "Provide valid PanCard number")
        @Column(name="panCard_number",length = 10)
        private String customerPanCardNumber;

        @NotBlank(message = "Provide valid Aadhar number")
        @Column(name="aadhar_card_number",length = 12)
        private String customerAadharCardNumber;

        @Column(name="date_of_birth")
        private String customerDateOfBirth;
        private String address;
        private String pinCode;

//        @OneToMany(targetEntity = Account.class,cascade = CascadeType.ALL)
//        @JoinColumn(name = "customerId",referencedColumnName = "customerId")
//        private List<Account> account;

        @Column(updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private Date CreatedDate = new Date();


}
