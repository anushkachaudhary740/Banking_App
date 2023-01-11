package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.responses.AllTransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.responses.TransactionsResponse;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionServices transactionServices;
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<AllTransactionsResponse> getTransactionDetails(@PathVariable("accountNumber") Integer accountNumber) {
        AllTransactionsResponse list=this.transactionServices.getAllTransactionDetailsForOneAccount(accountNumber);
        HttpStatus httpStatus=list.getSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(list,httpStatus);
    }
    @GetMapping("/get/{transactionId}")
    public ResponseEntity<Optional<TransactionDto>> findTransactionDetailsById(@PathVariable("transactionId") Integer transactionId){
        Optional<TransactionDto> transactionDto = (this.transactionServices.getTransactionDetailsById(transactionId));
        HttpStatus httpStatus=transactionDto.isPresent()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(transactionDto,httpStatus);

    }
    @PutMapping("/deposit")
    public ResponseEntity<TransactionsResponse> depositAmount(@Valid @PathParam("accountNumber") Integer accountNumber, @PathParam("depositAmount") Double depositAmount) {
        TransactionsResponse transactionsResponse =transactionServices.depositAmount(accountNumber, depositAmount);
        HttpStatus httpStatus=transactionsResponse.getSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(transactionsResponse,httpStatus);
    }
    @PutMapping(value = "/withdraw")
    public ResponseEntity<TransactionsResponse> withdrawAmount(@Valid @PathParam("accountNumber") Integer accountNumber, @PathParam("withdrawAmount") Double withdrawAmount) {
        TransactionsResponse transactionsResponse =this.transactionServices.withdrawAmount(accountNumber,withdrawAmount);
        HttpStatus httpStatus=transactionsResponse.getSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(transactionsResponse,httpStatus);
    }
    @PutMapping("/amount/transfer")
    public ResponseEntity<TransactionsResponse> moneyTransfer(@Valid @PathParam("senderAccountNumber") Integer senderAccountNumber, @PathParam("receiverAccountNumber") Integer receiverAccountNumber, @PathParam("amount") Double amount) {
    TransactionsResponse transactionsResponse =transactionServices.transferAmount(senderAccountNumber, receiverAccountNumber, amount);
    HttpStatus httpStatus=transactionsResponse.getSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(transactionsResponse,httpStatus);
    }

}
