package org.skillfactory.sf_bank_system.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BudgetDto {
    private Long id;
    private String category;
    @Min(value = 0, message = "Limit amount cannot be negative")
    private Integer limitAmount;
    private Integer currentAmount;
    private LocalDate validUntil;
}
