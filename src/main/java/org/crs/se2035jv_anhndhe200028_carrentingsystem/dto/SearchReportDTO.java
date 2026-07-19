package org.crs.se2035jv_anhndhe200028_carrentingsystem.dto;

import jakarta.validation.constraints.AssertTrue;
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

    @AssertTrue(message = "Ngày bắt đầu báo cáo phải nhỏ hơn hoặc bằng ngày kết thúc.")
    public boolean isDateRangeValid() {
        return pickupDate == null || returnDate == null || !pickupDate.isAfter(returnDate);
    }
}
