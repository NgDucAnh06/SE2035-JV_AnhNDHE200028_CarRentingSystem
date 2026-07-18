package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.ReviewRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarRentalRepository carRentalRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void save(Car car) {
        if (carRepository.findByCarName(car.getCarName()) != null) {
            throw new DuplicateResourceException("Car name already exists!");
        }
        carRepository.save(car);
    }

    @Override
    public void update(Car car) {
        Car oldCar = carRepository.findByCarName(car.getCarName());
        if (oldCar != null && !oldCar.getCarID().equals(car.getCarID())) {
            throw new DuplicateResourceException("Car name  already exists!");
        }
        carRepository.save(car);
    }

    @Override
    public List<Car> getAll() {
        return carRepository.findAll();
    }

    @Override
    public Car findByCarName(String carName) {
        return carRepository.findByCarName(carName);
    }

    @Override
    public List<Car> findAvailableCars() {
        return carRepository.findByStatus("AVAILABLE");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Car> searchAvailableCars(String keyword, Pageable pageable) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        return carRepository.searchAvailableCars(normalizedKeyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Car> searchCarsByName(String keyword, Pageable pageable) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        return carRepository.findByCarNameContainingIgnoreCase(normalizedKeyword, pageable);
    }

    @Override
    public Car findById(Integer id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCar(Integer id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found."));
        List<CarRental> rentals = carRentalRepository.findAllByCar(car);

        if (rentals.stream().anyMatch(this::isActiveRental)) {
            throw new IllegalStateException(
                    "Cannot delete this car because it is waiting for pickup or currently renting."
            );
        }

        deleteRentalHistory(rentals);
        carRepository.delete(car);
    }

    private boolean isActiveRental(CarRental rental) {
        return RentalStatus.WAITING_FOR_PICKUP.name().equals(rental.getStatus())
                || RentalStatus.RENTING.name().equals(rental.getStatus());
    }

    private void deleteRentalHistory(List<CarRental> rentals) {
        if (rentals.isEmpty()) {
            return;
        }
        reviewRepository.deleteAll(reviewRepository.findByCarRentalIn(rentals));
        reviewRepository.flush();
        carRentalRepository.deleteAll(rentals);
        carRentalRepository.flush();
    }
}
