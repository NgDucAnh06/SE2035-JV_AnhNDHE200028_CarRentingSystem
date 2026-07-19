package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarProducerService {
    void save(CarProducer carProducer);
    void update(CarProducer carProducer);
    CarProducer findByProducerName(String producerName);
    CarProducer findByProducerID(Integer id);
    List<CarProducer> getAll();
    Page<CarProducer> searchProducersByName(String keyword, Pageable pageable);
    void deleteProducer(Integer id);
}
