package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRentalRepository extends JpaRepository<CarRental, Integer> {
    List<CarRental> getAllByCustomerOrderByCarRenIDDesc(Customer customer);
    @Query("SELECT cr.carRenID AS carRenID, ct.fullName AS fullName, c.carName AS carName, " +
            "cr.pickupDate AS pickupDate, cr.returnDate AS returnDate, " +
            "cr.rentPrice AS rentPrice, cr.status AS status " +
            "FROM CarRental cr " +
            "JOIN cr.customer ct " +
            "JOIN cr.car c")
    List<RentalSummaryDTO> findCarRentalSummaries();
}
