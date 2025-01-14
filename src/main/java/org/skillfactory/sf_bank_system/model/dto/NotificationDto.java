package org.skillfactory.sf_bank_system.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

// или LocalDateTime — как у вас в сущности Notification
@Data
@Builder
public class NotificationDto {
    private Long id;
    private LocalDate date;
    private String message;
}
