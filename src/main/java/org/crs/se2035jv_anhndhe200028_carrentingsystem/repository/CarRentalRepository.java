package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    @Query("SELECT cr.carRenID AS carRenID, ct.fullName AS fullName, c.carName AS carName, " +
            "cr.pickupDate AS pickupDate, cr.returnDate AS returnDate, " +
            "cr.rentPrice AS rentPrice, cr.status AS status " +
            "FROM CarRental cr JOIN cr.customer ct JOIN cr.car c " +
            "WHERE (:pickupDate IS NULL OR cr.pickupDate = :pickupDate) AND " +
            "(:returnDate IS NULL OR cr.returnDate = :returnDate) AND " +
            "(:fullName IS NULL OR ct.fullName LIKE CONCAT('%', :fullName, '%'))")
    List<RentalSummaryDTO> findCarRentalReport(@Param("pickupDate") LocalDate pickupDate,
                                               @Param("returnDate") LocalDate returnDate,
                                               @Param("fullName") String fullName);


}
