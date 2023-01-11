package com.cognologix.BankSystemApplicationAssignment.dao;

import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    Customer findByCustomerId(Integer id);
//    @Query(value = "SELECT * FROM customer WHERE aadhar_card_number=?1 " +
//            "OR pan_card_number=?2", nativeQuery = true)
    Customer findByCustomerAadharCardNumberAndCustomerPanCardNumber(String adharNumber, String panCardNumber);
}
