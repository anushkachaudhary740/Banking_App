package com.cognologix.BankSystemApplicationAssignment.converter.loanconverter;

import com.cognologix.BankSystemApplicationAssignment.dto.LoanDto;
import com.cognologix.BankSystemApplicationAssignment.model.Loan;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanModelToDtoConverter {
    @Autowired
    private ModelMapper modelMapper;
    public LoanDto modelToDto(Loan loan){
        LoanDto loanDto=this.modelMapper.map(loan,LoanDto.class);
        return loanDto;
    }
}
