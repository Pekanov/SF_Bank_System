package org.skillfactory.sf_bank_system.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.*;
import org.skillfactory.sf_bank_system.model.entity.TransactionType;

import java.time.LocalDate;

@Data
@Builder
public class TransactionDto {
    private Long id;

    @NotNull(message = "Transaction type must not be null")
    private TransactionType type;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than 0")
    private Integer amount;

    private String category;

    private String name;

    private LocalDate date;

}
