package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.service.services.AccountServices;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountServices accountServices;
    @PostMapping("/create")
    public ResponseEntity<AccountDto> saveAccount(@RequestBody AccountDto account){
        accountServices.createAccount(account);
        return new ResponseEntity<>(account,HttpStatus.CREATED);
    }
    @GetMapping("/get")
    public ResponseEntity<List<AccountDto>> getAccountDetails() {
        List<AccountDto> list=this.accountServices.getAccountDetails();
        return new ResponseEntity<>(list,HttpStatus.CREATED);
    }
    @GetMapping("/get/{accountNumber}")
    public ResponseEntity<AccountDto> findAccountDetailsById( @PathVariable("accountNumber") Integer accountNumber){
        return ResponseEntity.ok(this.accountServices.getAccountByNumber(accountNumber));
    }
    @PutMapping("/update/{accountNumber}")
    public ResponseEntity<?> updateAccountDetails(@RequestBody AccountDto accountDto,@PathVariable("accountNumber") Integer accountNumber){
        this.accountServices.updateAccount(accountDto,accountNumber);
        return new ResponseEntity<>("Account updated successfully..", HttpStatus.OK);

    }
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<String> deleteAccountDetailsById(@PathVariable("accountNumber") int accountNumber){
        this.accountServices.deleteAccount(accountNumber);
        return new ResponseEntity<>("Account deleted successfully..", HttpStatus.OK);
    }

}

