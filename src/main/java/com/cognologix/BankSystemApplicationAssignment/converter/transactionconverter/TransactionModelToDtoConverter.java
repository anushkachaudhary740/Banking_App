package com.cognologix.BankSystemApplicationAssignment.converter.transactionconverter;

import com.cognologix.BankSystemApplicationAssignment.dto.TransactionDto;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionModelToDtoConverter {
    @Autowired
    private ModelMapper modelMapper;
    public TransactionDto transactionModelToDto(Transaction transaction){
        TransactionDto transDto=this.modelMapper.map(transaction,TransactionDto.class);
        return transDto;
    }
}
