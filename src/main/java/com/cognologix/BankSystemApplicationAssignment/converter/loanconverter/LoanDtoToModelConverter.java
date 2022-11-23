package com.cognologix.BankSystemApplicationAssignment.converter.loanconverter;

import com.cognologix.BankSystemApplicationAssignment.dto.LoanDto;
import com.cognologix.BankSystemApplicationAssignment.model.Loan;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanDtoToModelConverter {
    @Autowired
    private ModelMapper modelMapper;
    public Loan dtoToModel(LoanDto loanDto){
        Loan loanModel1=this.modelMapper.map(loanDto,Loan.class);
        return loanModel1;
    }
}
