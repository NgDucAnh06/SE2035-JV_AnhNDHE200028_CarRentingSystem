package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchReportDTO {
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String fullName;
    private String carName;
    private String status;
}
