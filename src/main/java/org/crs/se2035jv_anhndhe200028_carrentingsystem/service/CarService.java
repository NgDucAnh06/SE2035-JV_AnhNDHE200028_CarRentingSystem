package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;

public interface CarService {
    void save(Car car);
    Car findByCarName(String carName);
}
