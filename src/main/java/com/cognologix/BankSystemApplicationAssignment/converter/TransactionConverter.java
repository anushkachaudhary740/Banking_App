package com.cognologix.BankSystemApplicationAssignment.converter;

import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter {
    @Autowired
    private ModelMapper modelMapper;
    //dto to model converter
    public Transaction transactionDtoToModel(TransactionDto transactionDto){
        Transaction transModel=this.modelMapper.map(transactionDto,Transaction.class);
        return transModel;
    }
    //model to dto converter
    public TransactionDto transactionModelToDto(Transaction transaction){
        TransactionDto transDto=this.modelMapper.map(transaction,TransactionDto.class);
        return transDto;
    }
}
