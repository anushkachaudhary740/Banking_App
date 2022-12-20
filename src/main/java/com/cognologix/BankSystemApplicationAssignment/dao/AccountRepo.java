package com.cognologix.BankSystemApplicationAssignment.dao;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account,Integer> {
    }
