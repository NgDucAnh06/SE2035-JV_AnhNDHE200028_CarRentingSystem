package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsByCarRental(CarRental carRental);
    boolean existsByCarRental_CarRenID(Integer carRenID);
    Optional<Review> findByCarRentalIn(List<CarRental> carRentals);
    Optional<Review> findByCarRental(CarRental carRental);
}
