package org.skillfactory.sf_bank_system.model.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    String message;
    String classError;
    String methodError;
}

