package com.cognologix.BankSystemApplicationAssignment.converter.transactionconverter;

import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionDtoToModelConverter {
    @Autowired
    private ModelMapper modelMapper;
    public Transaction transactionDtoToModel(TransactionDto transactionDto){
        Transaction transModel=this.modelMapper.map(transactionDto,Transaction.class);
        return transModel;
    }
}
