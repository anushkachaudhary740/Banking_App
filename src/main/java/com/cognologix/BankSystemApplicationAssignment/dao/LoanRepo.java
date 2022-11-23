package com.cognologix.BankSystemApplicationAssignment.dao;

import com.cognologix.BankSystemApplicationAssignment.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LoanRepo extends JpaRepository<Loan,Integer> {
}
