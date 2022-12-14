package com.cognologix.BankSystemApplicationAssignment.dao;

import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Integer> {
}
