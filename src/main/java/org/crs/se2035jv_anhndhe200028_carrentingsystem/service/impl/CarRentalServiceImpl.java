package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRentalRepository carRentalRepository;
    private final CarRepository carRepository;

    @Override
    public void save(CarRental carRental) {
        if (carRental.getStatus() == null) {
            carRental.setStatus("PENDING");
        }
        
        carRentalRepository.save(carRental);

        if (carRental.getCar() != null && carRental.getCar().getCarID() != null) {
            Car car = carRepository.findById(carRental.getCar().getCarID())
                    .orElseThrow(() -> new IllegalArgumentException("Car not found"));
            car.setStatus("RENTED");
            carRepository.save(car);
        }
    }
}
