package org.skillfactory.sf_bank_system.model.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;


import java.time.ZonedDateTime;

@Builder
@Data
public class RegisterDto {

    @NotBlank(message = "The field 'email' should not be empty")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "The field 'password' should not be empty")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must have at least 8 characters, including at least one uppercase letter and one digit.")
    String password;

}
