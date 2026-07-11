package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;

import java.util.List;

public interface CarService {
    void save(Car car);
    void update(Car car);
    Car findByCarName(String carName);
    List<Car> findAvailableCars();
    Car findById(Integer id);
}
