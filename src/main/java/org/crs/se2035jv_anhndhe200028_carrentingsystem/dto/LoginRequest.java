package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Account name is required!")
    private String accountName;

    @NotBlank(message = "Password is required!")
    private String password;
}
