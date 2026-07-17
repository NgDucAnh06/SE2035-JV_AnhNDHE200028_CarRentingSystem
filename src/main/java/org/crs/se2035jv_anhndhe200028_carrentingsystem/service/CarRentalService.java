package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchReportDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CarRentalService {
    void save(CarRental carRental);
    void createRentals(Customer customer, List<Integer> carIds, LocalDate pickupDate, LocalDate returnDate);
    Page<CarRental> getCarRentalPageByCustomer(Customer customer, Pageable pageable);
    List<RentalSummaryDTO> showManagement();
    void updateStatus(Integer id, String status);
    List<RentalSummaryDTO> showReport(SearchReportDTO searchReportDTO);
}
