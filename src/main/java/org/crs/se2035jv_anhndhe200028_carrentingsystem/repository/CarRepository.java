package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Car findByCarName(String carName);

    List<Car> findByStatus(String status);
}
