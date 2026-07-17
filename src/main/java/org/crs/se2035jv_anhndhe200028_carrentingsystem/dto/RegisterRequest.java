package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Fullname is required!")
    private String fullName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Mobile is required!")
    @Pattern(
            regexp = "^(0)[0-9]{9}$",
            message = "Invalid mobile format!")
    private String mobile;

    @NotNull(message = "Birthday is required!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past!")
    private LocalDate birthday;

    @NotBlank(message = "Identity card is required!")
    @Pattern(regexp = "^0[0-9]{11}$", message = "Invalid format!")
    private String identityCard;

    @NotBlank(message = "Licence number is required!")
    @Pattern(regexp = "^0[0-9]{11}$", message = "Invalid format!")
    private String licenceNum;

    @NotNull(message = "Licence date is required!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Date of licence must be in the future!")
    private LocalDate licenceDate;

    @NotBlank(message = "Account name is required!")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9_]{4,19}$",
            message = "Account name must start with a letter and contain only letters, numbers, or underscore (5-20 characters)"
    )
    private String accountName;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Password must be at least 8 characters!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$",
            message = "Password must contain at least 1 uppercase letter and 1 digit!"
    )
    private String password;
}
