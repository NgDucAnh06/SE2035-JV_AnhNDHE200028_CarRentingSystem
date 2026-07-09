package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassRequestDTO {

    @NotBlank(message = "Old password is required!")
    private String oldPassword;

    @NotBlank(message = "New password is required!")
    @Size(min = 8, message = "Password must be at least 8 characters!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$",
            message = "Password must contain at least 1 uppercase letter and 1 digit!"
    )
    private String newPassword;

    @NotBlank(message = "Confirm password is required!")
    private String confirmPassword;
}
