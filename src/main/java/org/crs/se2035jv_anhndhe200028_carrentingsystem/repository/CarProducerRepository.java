package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarProducerRepository extends JpaRepository<CarProducer, Integer> {
    Optional<CarProducer> findByProducerName(String producerName);
    Optional<CarProducer> findByProducerID(Integer id);
    Page<CarProducer> findByProducerNameContainingIgnoreCase(String keyword, Pageable pageable);
}
