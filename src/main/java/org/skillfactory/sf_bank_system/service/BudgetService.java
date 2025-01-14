package org.skillfactory.sf_bank_system.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.dto.CreateBudgetDto;
import org.skillfactory.sf_bank_system.model.entity.Budget;
import org.skillfactory.sf_bank_system.model.entity.User;
import org.skillfactory.sf_bank_system.model.entity.Wallet;
import org.skillfactory.sf_bank_system.repository.BudgetRepository;
import org.skillfactory.sf_bank_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBudget(Long userId, CreateBudgetDto createBudgetDto) {
        // 1. Находим пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + userId));

        // 2. Берём первый (и фактически единственный) кошелёк пользователя
        Wallet wallet = user.getWallets().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Wallet not found for user with id " + userId));



        if(budgetRepository.existsBudgetByCategory(createBudgetDto.getCategory())) {
            Budget budget = budgetRepository.findByWalletIdAndCategory(wallet.getId(), createBudgetDto.getCategory())
                    .orElseThrow(() -> new NoSuchElementException("Budget not found for user with id " + userId));
            budget.setCategory(createBudgetDto.getCategory());
            budgetRepository.save(budget);
        }else{
            try {
                Budget newBudget = Budget.builder()
                        .wallet(wallet)
                        .category(createBudgetDto.getCategory())
                        .limitAmount(createBudgetDto.getLimitAmount())
                        .currentAmount(0)
                        .build();
                budgetRepository.save(newBudget);
            }catch (RuntimeException e) {
                throw new RuntimeException("Failde to create new budget", e);
            }

        }

    }
}
