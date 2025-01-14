package org.skillfactory.sf_bank_system.model.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.skillfactory.sf_bank_system.model.entity.Wallet;

import java.time.LocalDate;

@Data
@Builder
public class CreateBudgetDto {
    @NotBlank(message = "Category must not be blank")
    private String category;

    @Min(value = 0, message = "limitAmount cannot be negative")
    private Integer limitAmount;
}
