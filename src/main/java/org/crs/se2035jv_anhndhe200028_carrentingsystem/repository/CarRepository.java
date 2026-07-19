package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Optional<Car> findByCarName(String carName);

    List<Car> findByStatus(CarStatus status);

    List<Car> findAllByProducer(CarProducer producer);

    Page<Car> findByCarNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("""
            SELECT c FROM Car c
            JOIN c.producer p
            WHERE c.status = 'AVAILABLE'
              AND (:keyword = ''
                   OR LOWER(c.carName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.producerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(c.color) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Car> searchAvailableCars(@Param("keyword") String keyword, Pageable pageable);
}
