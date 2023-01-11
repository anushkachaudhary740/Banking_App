package com.cognologix.BankSystemApplicationAssignment.dao;

import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepo extends JpaRepository<Transaction,Integer> {
    @Query(value = "SELECT * FROM transaction" +
            " WHERE from_account_number=?1 OR to_account_number=?1", nativeQuery = true)
    List<Transaction> findByToAccountNumber(Integer toAccountNumber);

}

