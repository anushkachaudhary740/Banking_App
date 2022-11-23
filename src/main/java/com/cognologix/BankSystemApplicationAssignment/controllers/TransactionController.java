package com.cognologix.BankSystemApplicationAssignment.controllers;
import com.cognologix.BankSystemApplicationAssignment.service.services.TransactionServices;
import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionServices transactionServices;
    @PostMapping("/create")
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto){
        transactionServices.createTransaction(transactionDto);
        return new ResponseEntity<>(transactionDto,HttpStatus.CREATED);
    }
    @GetMapping("/get")
    public ResponseEntity<List<TransactionDto>> getTransactionDetails() {
        List<TransactionDto> list=this.transactionServices.getTransactionDetails();
        return new ResponseEntity<>(list,HttpStatus.CREATED);
    }
    @PutMapping(value = "/deposit")
    public ResponseEntity<?> depositAmount(@PathParam("accountNumber") Integer accountNumber, @PathParam("depositAmount") Double depositAmount) {
        System.out.println("deposit...."+depositAmount+"  account number "+accountNumber);
        transactionServices.getDeposit(accountNumber, depositAmount);
        return new ResponseEntity<String>("Rs "+depositAmount+" Successfully deposit.....", HttpStatus.OK);
    }
    @PutMapping(value = "/withdraw")
    public ResponseEntity<?> withdrawAmount(@PathParam("accountNumber") Integer accountNumber,@PathParam("withdrawAmount") Double withdrawAmount) {
        System.out.println("withdraw......"+withdrawAmount+"  account number "+accountNumber);
        this.transactionServices.getWithDraw(accountNumber,withdrawAmount);
        return new ResponseEntity<String>( "Rs "+withdrawAmount+" Successfully withdraw......",HttpStatus.OK);

    }
    @PutMapping("/amount/transfer")
    public ResponseEntity<?> moneyTransfer(@PathParam("senderId") Integer senderId, @PathParam("receiverId") Integer receiverId, @PathParam("amount") Double amount) {
        transactionServices.amountTransfer(senderId, receiverId, amount);
        System.out.println("id1:"+senderId+"id2"+receiverId+amount);
        return new ResponseEntity<String >("Rs "+amount+" successfully transfer......", HttpStatus.OK);
    }
    @GetMapping("/amount/{accountNumber}")
    public ResponseEntity<?> findTotalBalance( @PathVariable("accountNumber") int accountNumber){
        Double currentBalance=transactionServices.getTotalBalance(accountNumber);
        return new ResponseEntity<String>("Total balance : "+currentBalance, HttpStatus.OK);
    }
}
