package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UpdateProfileRequest {
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
}
