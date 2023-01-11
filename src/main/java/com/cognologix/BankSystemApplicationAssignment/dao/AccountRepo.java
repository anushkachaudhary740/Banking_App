package com.cognologix.BankSystemApplicationAssignment.dao;
import com.cognologix.BankSystemApplicationAssignment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account,Integer> {
    @Query(value = "SELECT * FROM account WHERE customer_customer_id=?1", nativeQuery = true)
    List<Account> getAllAccountsForACustomer(Integer customerId);
}
