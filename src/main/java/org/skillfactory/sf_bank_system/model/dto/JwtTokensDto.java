package org.skillfactory.sf_bank_system.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokensDto {

    @NotBlank(message = "The field 'accessToken' should not be empty")
    private String accessToken;

    @NotBlank(message = "The field 'refreshToken' should not be empty")
    private String refreshToken;

}