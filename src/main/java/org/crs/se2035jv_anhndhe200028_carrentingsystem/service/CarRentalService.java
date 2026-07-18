package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalReportStatsDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchHistoryRequest;
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
    Page<CarRental> showHistoryByCustomer(Customer customer, Pageable pageable, SearchHistoryRequest searchHistoryRequest);
    Page<RentalSummaryDTO> showManagement(SearchHistoryRequest searchRequest, Pageable pageable);
    void updateStatus(Integer id, String status);
    void cancelRentalByCustomer(Integer id, Customer customer);
    Page<RentalSummaryDTO> showReport(SearchReportDTO searchReportDTO, Pageable pageable);
    RentalReportStatsDTO getReportStats(SearchReportDTO searchReportDTO);
    Page<CarRental> getCompletedRentals(Customer customer, Pageable pageable);
    CarRental findById(Integer id);
}
