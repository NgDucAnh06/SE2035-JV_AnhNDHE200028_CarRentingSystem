package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;

public interface CarProducerService {
    void save(CarProducer carProducer);
    CarProducer findByProducerName(String producerName);
}
