package org.skillfactory.sf_bank_system.service;


import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.repository.BudgetRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;


}
