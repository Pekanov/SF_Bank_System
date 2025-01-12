package org.skillfactory.sf_bank_system.service;


import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.dto.TransactionalDto;
import org.skillfactory.sf_bank_system.model.entity.Transaction;
import org.skillfactory.sf_bank_system.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void addTransaction(TransactionalDto transactionalDto) {


        Transaction transaction = Transaction.builder()

                .build();

    }

}
