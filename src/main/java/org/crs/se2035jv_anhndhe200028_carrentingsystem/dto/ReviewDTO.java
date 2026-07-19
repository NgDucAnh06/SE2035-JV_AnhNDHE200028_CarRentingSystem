package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Integer carRenID;

    @NotNull(message = "Review star is required!")
    @Min(value = 1, message = "Review star must be at least 1")
    @Max(value = 5, message = "Review star cannot exceed 5")
    private Integer reviewStar;

    @NotBlank(message = "Comment is required!")
    private String comment;
}
