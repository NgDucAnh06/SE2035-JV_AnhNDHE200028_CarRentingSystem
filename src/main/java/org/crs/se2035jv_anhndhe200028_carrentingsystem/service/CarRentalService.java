package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;

import java.util.List;

public interface CarRentalService {
    void save(CarRental carRental);
    void createRentals(Customer customer, List<Integer> carIds, java.time.LocalDate pickupDate, java.time.LocalDate returnDate);
    List<CarRental> getAllCarRentalByCustomer(Customer customer);
    List<RentalSummaryDTO> showManagement();
    void updateStatus(Integer id, String status);
}
