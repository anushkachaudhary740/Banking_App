package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.AccountStatusResponce;
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
    public ResponseEntity<AccountResponse> createAccount( @RequestBody AccountDto accountDto){
        AccountResponse newAccountDto=accountServices.createAccount(accountDto);
        return new ResponseEntity<>(newAccountDto,HttpStatus.CREATED);
    }
    @GetMapping("/get")
    public ResponseEntity<List<AccountDto>> getAccountDetails() {
        List<AccountDto> list=this.accountServices.getAccountDetails();
        HttpStatus status=list.size()>0?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(list,status);
    }
    @GetMapping("/get/{accountNumber}")
    public ResponseEntity<Optional<AccountDto>> findAccountDetailsByCustomerId(@PathVariable("accountNumber") Integer accountNumber){
        Optional<AccountDto> accountDto=this.accountServices.getAccountDetailsByNumber(accountNumber);
        HttpStatus status=accountDto.isPresent()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(accountDto,status);
    }
    @PutMapping("/update/{accountNumber}")
    public ResponseEntity<AccountResponse> updateAccountDetails(@Valid @RequestBody AccountDto accountDto,@PathVariable("accountNumber") Integer accountNumber){
        AccountResponse newAccountDto=accountServices.updateAccount(accountDto,accountNumber);
        //HttpStatus status=newAccountDto.getSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(newAccountDto,HttpStatus.OK);

    }
    @GetMapping("/amount/{accountNumber}")
    public ResponseEntity<AccountResponse> findTotalBalance(@PathVariable("accountNumber") Integer accountNumber){
        AccountResponse accountResponse=accountServices.getTotalBalance(accountNumber);
        HttpStatus status=accountResponse.getSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(accountResponse, status);
    }
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<AccountResponse> deleteAccountDetailsById(@PathVariable("accountNumber") Integer accountNumber){
        AccountResponse accountResponse=this.accountServices.deleteAccount(accountNumber);
        HttpStatus status=accountResponse.getSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(accountResponse, status);
    }
    @PutMapping("/activate/{accountNumber}")
    public ResponseEntity<AccountStatusResponce> activateAccount(@PathVariable("accountNumber") Integer accountNumber){
        AccountStatusResponce activateAccountResponse = this.accountServices.activateAccount(accountNumber);
        //LOGGER.info(activateAccountResponse.toString());
        HttpStatus httpStatus = activateAccountResponse.getSuccess()?HttpStatus.OK:HttpStatus.BAD_REQUEST;
        //LOGGER.info(httpStatus.toString());
        return new ResponseEntity<>(activateAccountResponse,httpStatus);
    }
    @PutMapping("/deactivate/{accountNumber}")
    public ResponseEntity<AccountStatusResponce> deactivateAccount(@PathVariable("accountNumber") Integer accountNumber){
        AccountStatusResponce deactivateAccountResponse = this.accountServices.deactivateAccount(accountNumber);
       // LOGGER.info(deactivateAccountResponse.toString());
        HttpStatus httpStatus = deactivateAccountResponse.getSuccess()?HttpStatus.OK:HttpStatus.BAD_REQUEST;
       // LOGGER.info(httpStatus.toString());
        return new ResponseEntity<>(deactivateAccountResponse,httpStatus);
    }

}

