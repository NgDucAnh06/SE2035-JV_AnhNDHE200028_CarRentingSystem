package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RentalSummaryDTO {
    Integer getCarRenID();
    String getFullName();
    String getCarName();
    LocalDate getPickupDate();
    LocalDate getReturnDate();
    BigDecimal getRentPrice();
    String getStatus();
}
