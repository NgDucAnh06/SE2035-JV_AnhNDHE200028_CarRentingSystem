package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Review;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.ReviewRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CarRentalRepository carRentalRepository;

    @Override
    public void submitReview(Integer carRenId, Integer stars, String comment) {
        if (reviewRepository.existsByCarRental_CarRenID(carRenId)) {
            throw new IllegalArgumentException("This rental has already been reviewed.");
        }

        CarRental carRental = carRentalRepository.findById(carRenId)
                .orElseThrow(() -> new IllegalArgumentException("Car rental not found"));

        if (!"COMPLETED".equals(carRental.getStatus())) {
            throw new IllegalArgumentException("Can only review completed rentals.");
        }

        Review review = Review.builder()
                .carRental(carRental)
                .reviewStar(stars)
                .comment(comment)
                .build();

        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getReviewedRentalIds(List<CarRental> rentals) {
        if (rentals == null || rentals.isEmpty()) {
            return Collections.emptyList();
        }

        return reviewRepository.findByCarRentalIn(rentals).stream()
                .map(review -> review.getCarRental().getCarRenID())
                .toList();
    }

    @Override
    public Review getReview(CarRental carRental) {
        return reviewRepository.findByCarRental(carRental).orElse(null);
    }
}
