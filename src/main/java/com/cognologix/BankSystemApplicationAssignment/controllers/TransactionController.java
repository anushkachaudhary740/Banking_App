package com.cognologix.BankSystemApplicationAssignment.controllers;
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

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionServices transactionServices;
    @GetMapping("/get")
    public ResponseEntity<List<TransactionDto>> getTransactionDetails() {
        List<TransactionDto> list=this.transactionServices.getTransactionDetails();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    @GetMapping("/get/{transactionId}")
    public ResponseEntity<TransactionDto> findTransactionDetailsById(@PathVariable("transactionId") Integer transactionId){
        return ResponseEntity.ok(this.transactionServices.getTransactionDetailsById(transactionId));
    }
    @PutMapping("/deposit")
    public ResponseEntity<TransactionsResponse> depositAmount(@Valid @PathParam("accountNumber") Integer accountNumber, @PathParam("depositAmount") Double depositAmount) {
        TransactionsResponse transactionsResponse =transactionServices.depositAmount(accountNumber, depositAmount);
        return new ResponseEntity<>(transactionsResponse,HttpStatus.OK);
    }
    @PutMapping(value = "/withdraw")
    public ResponseEntity<TransactionsResponse> withdrawAmount(@Valid @PathParam("accountNumber") Integer accountNumber, @PathParam("withdrawAmount") Double withdrawAmount) {
        TransactionsResponse transactionsResponse =this.transactionServices.withdrawAmount(accountNumber,withdrawAmount);
        return new ResponseEntity<>(transactionsResponse,HttpStatus.OK);
    }
    @PutMapping("/amount/transfer")
    public ResponseEntity<TransactionsResponse> moneyTransfer(@Valid @PathParam("senderAccountNumber") Integer senderAccountNumber, @PathParam("receiverAccountNumber") Integer receiverAccountNumber, @PathParam("amount") Double amount) {
    TransactionsResponse transactionsResponse =transactionServices.transferAmount(senderAccountNumber, receiverAccountNumber, amount);
        return new ResponseEntity<>(transactionsResponse,HttpStatus.OK);
    }

}
