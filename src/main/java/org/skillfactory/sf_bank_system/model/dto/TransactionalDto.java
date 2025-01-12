package org.skillfactory.sf_bank_system.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.entity.TransactionType;
import org.skillfactory.sf_bank_system.model.entity.Wallet;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Builder
public class TransactionalDto {

    private TransactionType type;

    private Integer amount;

    private String category;

    private String name;

}
