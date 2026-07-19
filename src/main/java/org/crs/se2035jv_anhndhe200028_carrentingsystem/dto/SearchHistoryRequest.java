package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchHistoryRequest {
    private String carName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
    private String order;
}
