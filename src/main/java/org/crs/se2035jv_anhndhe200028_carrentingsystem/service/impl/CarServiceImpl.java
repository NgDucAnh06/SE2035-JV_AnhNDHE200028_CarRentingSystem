package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {
    
    private final CarRepository carRepository;

    @Override
    public void save(Car car) {
        if (carRepository.findByCarName(car.getCarName()) != null) {
            throw new DuplicateResourceException("Car name already exists!");
        }
        carRepository.save(car);
    }

    @Override
    public Car findByCarName(String carName) {
        return carRepository.findByCarName(carName);
    }
}
