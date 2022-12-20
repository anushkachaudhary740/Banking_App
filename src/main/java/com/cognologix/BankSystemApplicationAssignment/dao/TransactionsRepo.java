package com.cognologix.BankSystemApplicationAssignment.dao;

import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepo extends JpaRepository<Transaction,Integer> {
}
