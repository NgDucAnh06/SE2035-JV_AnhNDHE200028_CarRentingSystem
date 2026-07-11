package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarProducerRepository extends JpaRepository<CarProducer, Integer> {
    CarProducer findByProducerName(String producerName);
    CarProducer findByProducerID(Integer id);
}
