package com.cognologix.BankSystemApplicationAssignment.service.serviceInterfaces;

import com.cognologix.BankSystemApplicationAssignment.converter.Converter;
import com.cognologix.BankSystemApplicationAssignment.dao.TransactionsRepo;
import com.cognologix.BankSystemApplicationAssignment.dto.AmountTransferDto;
import com.cognologix.BankSystemApplicationAssignment.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServicesTest {
    @MockBean
    private TransactionServices transactionServices;

    private AmountTransferDto amountTransferDto;
    @MockBean
    private TransactionsRepo transactionsRepo;
    @Autowired
    private Converter transactionConverter;
    @BeforeEach
    void setUp(){
        amountTransferDto = AmountTransferDto.builder()
                .toAccountNumber(1)
                .fromAccountNumber(2)
                .transferAmount(0.00)
                .status("testing")
                .build();
    }
    @Test
    void getTransactionDetails() {
       Transaction transaction=this.transactionConverter.transferDtoToModel(amountTransferDto);
//        transactionsRepo.save(transaction);
//        transactionServices.getTransactionDetails();
//        verify(transactionsRepo).findAll();
//        when(transactionsRepo.findAll()).thenReturn((List<Transaction>) Stream
//                .of(new Transaction( 3234,1,2,700.0,"Testing")).collect(Collectors.toList()));
//        assertEquals(1, transactionServices.getTransactionDetails().size());

        transactionsRepo.save(transaction);
        System.out.println(transactionsRepo);
//            when(transactionsRepo.findAll()).thenReturn((List<Transaction>) Stream
//                    .of(new Transaction( 3234,1,2, 0.00,"Testing")).collect(Collectors.toList()));
//            assertEquals(1, transactionServices.getTransactionDetails().size());
//        System.out.println("size......"+transactionServices.getTransactionDetails().size());


    }

    @Test
    void getTransactionDetailsById() {
    }

    @Test
    void getDeposit() {
    }

    @Test
    void getWithDraw() {

    }

    @Test
    void amountTransfer() {
        Double amount =100.00;
        //Transaction accMoneyTransfer = mock(Transaction.class);
        AmountTransferDto accMoneyTransferSaveDto = mock(AmountTransferDto.class);

        //TransactionDto trans=transactionServices.amountTransfer(transactionDto.getFromAccountNumber(), transactionDto.getToAccountNumber(),amount);

        AmountTransferDto trans=transactionServices.amountTransfer(accMoneyTransferSaveDto.getToAccountNumber(),accMoneyTransferSaveDto.getFromAccountNumber(),amount);
        System.out.println(trans);
        Transaction transaction=this.transactionConverter.transferDtoToModel(trans);
        verify(transactionsRepo.save(transaction));

    }
}