package org.skillfactory.sf_bank_system.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserFullInfoDto {
    private Long userId;
    private String username;
    private String email;

    private Long walletId;
    private Integer walletBalance;

    private List<TransactionDto> transactions;
    private List<BudgetDto> budgets;
    private List<NotificationDto> notifications;
}
