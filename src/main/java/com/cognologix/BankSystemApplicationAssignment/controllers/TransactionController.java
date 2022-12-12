package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.dto.AmountTransferDto;
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
    public ResponseEntity<List<AmountTransferDto>> getTransactionDetails() {
        List<AmountTransferDto> list=this.transactionServices.getTransactionDetails();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    @GetMapping("/get/{transactionId}")
    public ResponseEntity<AmountTransferDto> findTransactionDetailsById(@PathVariable("transactionId") Integer transactionId){
        return ResponseEntity.ok(this.transactionServices.getTransactionDetailsById(transactionId));
    }
    @PutMapping("/deposit")
    public ResponseEntity<TransactionDto> depositAmount(@Valid @PathParam("accountNumber") Integer accountNumber, @PathParam("depositAmount") Double depositAmount) {
        TransactionDto transactionDto =transactionServices.deposit(accountNumber, depositAmount);
        //return new ResponseEntity<>(new ApiResponse("Rs "+depositAmount+" Successfully deposit.....",true), HttpStatus.OK);
        return new ResponseEntity<>(transactionDto,HttpStatus.OK);
    }
    @PutMapping(value = "/withdraw")
    public ResponseEntity<TransactionDto> withdrawAmount(@Valid @PathParam("accountNumber") Integer accountNumber, @PathParam("withdrawAmount") Double withdrawAmount) {
        TransactionDto withdrawDto =this.transactionServices.withDraw(accountNumber,withdrawAmount);
        //return new ResponseEntity<>( new ApiResponse("Rs "+withdrawAmount+" Successfully withdraw......",true),HttpStatus.OK);
        return new ResponseEntity<>(withdrawDto,HttpStatus.OK);
    }
    @PutMapping("/amount/transfer")
    public ResponseEntity<AmountTransferDto> moneyTransfer(@Valid @PathParam("senderAccountNumber") Integer senderAccountNumber, @PathParam("receiverAccountNumber") Integer receiverAccountNumber, @PathParam("amount") Double amount) {
        AmountTransferDto amountTransferDto =transactionServices.amountTransfer(senderAccountNumber, receiverAccountNumber, amount);
        //return new ResponseEntity<>(new ApiResponse("Rs "+amount+" successfully transfer......",true), HttpStatus.OK);
        return new ResponseEntity<>(amountTransferDto,HttpStatus.OK);
    }

}
