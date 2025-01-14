package org.skillfactory.sf_bank_system.service;

import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.dto.*;
import org.skillfactory.sf_bank_system.model.entity.*;
import org.skillfactory.sf_bank_system.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public UserFullInfoDto getUserFullInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(
                        "User not found with id: " + userId));


        Wallet wallet = user.getWallets().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Wallet not found for user " + userId));


        List<TransactionDto> transactionDtos = wallet.getTransactions().stream()
                .map(tx -> TransactionDto.builder()
                        .id(tx.getId())
                        .type(TransactionType.valueOf(tx.getType().toString()))
                        .amount(tx.getAmount())
                        .category(tx.getCategory())
                        .name(tx.getName())
                        .date(tx.getDate())
                        .build())
                .toList();


        List<BudgetDto> budgetDtos = wallet.getBudgets().stream()
                .map(bud -> BudgetDto.builder()
                        .id(bud.getId())
                        .category(bud.getCategory())
                        .limitAmount(bud.getLimitAmount())
                        .currentAmount(bud.getCurrentAmount())
                        .build())
                .toList();


        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        List<NotificationDto> notificationDtos = notifications.stream()
                .map(n -> NotificationDto.builder()
                        .id(n.getId())
                        .date(n.getDate())
                        .message(n.getMessage())
                        .build())
                .toList();

        return UserFullInfoDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .walletId(wallet.getId())
                .walletBalance(wallet.getBalance())
                .transactions(transactionDtos)
                .budgets(budgetDtos)
                .notifications(notificationDtos)
                .build();
    }
}
