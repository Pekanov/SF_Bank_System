package org.skillfactory.sf_bank_system.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.dto.TransactionDto;
import org.skillfactory.sf_bank_system.model.entity.*;
import org.skillfactory.sf_bank_system.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BudgetRepository budgetRepository;
    private final NotificationRepository notificationRepository;


    @Transactional
    public void addTransaction(TransactionDto transactionDto, Long userId) {

        // 1. Находим пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(
                        "User not found with id: " + userId));

        // 2. Берём кошелёк пользователя
        Wallet wallet = user.getWallets().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "User " + userId + " does not have a wallet."));

        // 3. Создаём транзакцию
        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .type(transactionDto.getType())
                .amount(transactionDto.getAmount())
                .category(transactionDto.getCategory()) // может быть null -> станет "unknown category" @PostPersist
                .name(transactionDto.getName())
                .date(LocalDate.now())
                .build();

        // 4. Сохраняем транзакцию
        transactionRepository.save(transaction);

        // 5. Если это расход (EXPENSE), обрабатываем бюджет
        if (transaction.getType() == TransactionType.EXPENSE) {
            String finalCategory = transaction.getCategory();
            // Оно либо пришло из DTO, либо уже "unknown category" в @PostPersist

            // Ищем, есть ли уже бюджет с этой категорией
            Budget budget = budgetRepository.findByWalletIdAndCategory(wallet.getId(), finalCategory)
                    .orElse(null);

            // Если бюджета нет — создаём безлимитный (limitAmount = 0)
            if (budget == null) {
                budget = Budget.builder()
                        .wallet(wallet)
                        .category(finalCategory)
                        .limitAmount(0)        // 0 = "нет лимита", по вашей логике
                        .currentAmount(0)      // начинаем с 0
                        .build();
            }

            // Увеличиваем текущий расход по этой категории
            int oldAmount = (budget.getCurrentAmount() == null) ? 0 : budget.getCurrentAmount();
            int newAmount = oldAmount + transaction.getAmount();
            budget.setCurrentAmount(newAmount);

            // Если нужно проверить лимит
            if (budget.getLimitAmount() > 0 && newAmount > budget.getLimitAmount()) {
                notificationRepository.save(
                        Notification.builder()
                                .user(user)
                                .message("Бюджет по категории '" + finalCategory
                                        + "' превышен. Лимит: " + budget.getLimitAmount()
                                        + ", текущий расход: " + newAmount)
                                .build()
                );
            }

            // Сохраняем изменения в бюджете
            budgetRepository.save(budget);

            // Также списываем средства из баланса кошелька
            int newBalance = wallet.getBalance() - transaction.getAmount();
            if (newBalance < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            wallet.setBalance(newBalance);
        }
        // 6. Если доход (INCOME), только увеличиваем баланс кошелька
        else if (transaction.getType() == TransactionType.INCOME) {
            wallet.setBalance(wallet.getBalance() + transaction.getAmount());
        }

        // 7. Сохраняем обновлённый кошелёк
        walletRepository.save(wallet);
    }

}
