package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.BaseResponse;
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
        AccountDto accountDto=accountServices.createAccount(account);
        return new ResponseEntity<>(accountDto,HttpStatus.CREATED);
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
    public ResponseEntity<AccountResponse> findTotalBalance(@PathVariable("accountNumber") Integer accountNumber){
        AccountResponse accountResponse=accountServices.getTotalBalance(accountNumber);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<AccountResponse> deleteAccountDetailsById(@PathVariable("accountNumber") Integer accountNumber){
        AccountResponse accountResponse=this.accountServices.deleteAccount(accountNumber);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

}

