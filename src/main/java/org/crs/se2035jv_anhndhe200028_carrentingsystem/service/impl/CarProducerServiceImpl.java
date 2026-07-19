package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Review;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarProducerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.ReviewRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarProducerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarProducerServiceImpl implements CarProducerService {

    private final CarProducerRepository carProducerRepository;
    private final CarRepository carRepository;
    private final CarRentalRepository carRentalRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void save(CarProducer carProducer) {
        if (carProducerRepository.findByProducerName(carProducer.getProducerName()).isPresent()) {
            throw new DuplicateResourceException("Producer name already exists!");
        }
        carProducerRepository.save(carProducer);
    }

    @Override
    public void update(CarProducer carProducer) {
        carProducerRepository.findByProducerName(carProducer.getProducerName())
                .filter(existing -> !existing.getProducerID().equals(carProducer.getProducerID()))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Producer name already exists!");
                });
        carProducerRepository.save(carProducer);
    }

    @Override
    public CarProducer findByProducerName(String producerName) {
        return carProducerRepository.findByProducerName(producerName).orElse(null);
    }

    @Override
    public CarProducer findByProducerID(Integer id) {
        return carProducerRepository.findByProducerID(id).orElse(null);
    }

    @Override
    public List<CarProducer> getAll() {
        return carProducerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarProducer> searchProducersByName(String keyword, Pageable pageable) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        return carProducerRepository.findByProducerNameContainingIgnoreCase(normalizedKeyword, pageable);
    }

    @Override
    public void deleteProducer(Integer id) {
        CarProducer producer = carProducerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producer not found."));
        List<Car> cars = carRepository.findAllByProducer(producer);
        List<CarRental> rentals = cars.isEmpty()
                ? List.of()
                : carRentalRepository.findAllByCarIn(cars);

        if (rentals.stream().anyMatch(this::isActiveRental)) {
            throw new IllegalStateException(
                    "Cannot delete this producer because one or more cars are waiting for pickup or currently renting."
            );
        }

        if (!rentals.isEmpty()) {
            List<Review> reviews = reviewRepository.findByCarRentalIn(rentals);
            if (!reviews.isEmpty()) {
                reviewRepository.deleteAll(reviews);
                reviewRepository.flush();
            }
            carRentalRepository.deleteAll(rentals);
            carRentalRepository.flush();
        }

        if (!cars.isEmpty()) {
            carRepository.deleteAll(cars);
            carRepository.flush();
        }
        carProducerRepository.delete(producer);
    }

    private boolean isActiveRental(CarRental rental) {
        return rental.getStatus() == RentalStatus.WAITING_FOR_PICKUP
                || rental.getStatus() == RentalStatus.RENTING;
    }
}
