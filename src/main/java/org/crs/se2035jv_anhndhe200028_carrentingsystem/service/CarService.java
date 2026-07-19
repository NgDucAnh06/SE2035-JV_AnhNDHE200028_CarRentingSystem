package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {
    void save(Car car);
    void update(Car car);
    List<Car> getAll();
    Car findByCarName(String carName);
    List<Car> findAvailableCars();
    Page<Car> searchAvailableCars(String keyword, Pageable pageable);
    Page<Car> searchCarsByName(String keyword, Pageable pageable);
    Car findById(Integer id);
    void deleteCar(Integer id);
}
