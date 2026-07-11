package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;

import java.util.List;

public interface CarProducerService {
    void save(CarProducer carProducer);
    void update(CarProducer carProducer);
    CarProducer findByProducerName(String producerName);
    CarProducer findByProducerID(Integer id);
    List<CarProducer> getAll();
}
