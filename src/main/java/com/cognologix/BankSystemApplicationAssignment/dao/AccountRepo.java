package com.cognologix.BankSystemApplicationAssignment.dao;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account,Integer> {
}
