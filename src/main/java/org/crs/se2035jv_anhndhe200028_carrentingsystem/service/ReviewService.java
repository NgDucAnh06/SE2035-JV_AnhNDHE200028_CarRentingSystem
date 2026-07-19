package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Review;

import java.util.List;

public interface ReviewService {
    void submitReview(Integer carRenId, Integer stars, String comment);
    List<Integer> getReviewedRentalIds(List<CarRental> rentals);
    Review getReview(CarRental carRental);
}
