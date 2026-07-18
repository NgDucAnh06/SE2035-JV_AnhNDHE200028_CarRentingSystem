package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RentalReportStatsDTO {
    private final long totalRentals;
    private final BigDecimal completedRevenue;
    private final long completedRentals;
    private final long activeRentals;
    private final long canceledRentals;
}
