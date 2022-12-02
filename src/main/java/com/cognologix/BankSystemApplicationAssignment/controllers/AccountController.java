package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.exceptions.ApiResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.AccountServices;
import com.cognologix.BankSystemApplicationAssignment.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountServices accountServices;
    @PostMapping("/create")
    public ResponseEntity<AccountDto> saveAccount(@Valid @RequestBody AccountDto account){
        accountServices.createAccount(account);
        return new ResponseEntity<>(account,HttpStatus.CREATED);
    }
    @GetMapping("/get")
    public ResponseEntity<List<AccountDto>> getAccountDetails() {
        List<AccountDto> list=this.accountServices.getAccountDetails();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    @GetMapping("/get/{accountNumber}")
    public ResponseEntity<Optional<AccountDto>> findAccountDetailsById(@PathVariable("accountNumber") Integer accountNumber){
        return ResponseEntity.ok(this.accountServices.getAccountDetailsByNumber(accountNumber));
    }
    @PutMapping("/update/{accountNumber}")
    public ResponseEntity<?> updateAccountDetails(@Valid @RequestBody AccountDto accountDto,@PathVariable("accountNumber") Integer accountNumber){
//        this.accountServices.updateAccount(accountDto,accountNumber);
//        return new ResponseEntity<>("Account updated successfully..", HttpStatus.OK);
        return accountServices.getAccountDetailsByNumber(accountNumber)
                .map(savedAccount -> {

                    savedAccount.setBankName(accountDto.getBankName());
                    savedAccount.setTypeOfAccount(accountDto.getTypeOfAccount());
                    savedAccount.setTotalAmount(accountDto.getTotalAmount());

                    AccountDto accountDto1=accountServices.updateAccount(savedAccount);
                    return new ResponseEntity<>(accountDto1, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
    @GetMapping("/amount/{accountNumber}")
    public ResponseEntity<ApiResponse> findTotalBalance( @PathVariable("accountNumber") Integer accountNumber){
        Double currentBalance=accountServices.getTotalBalance(accountNumber);
        return new ResponseEntity<>(new ApiResponse("Total balance : "+currentBalance,true), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<ApiResponse> deleteAccountDetailsById(@PathVariable("accountNumber") Integer accountNumber){
        this.accountServices.deleteAccount(accountNumber);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Account deleted successfully..",true), HttpStatus.OK);
    }

}

