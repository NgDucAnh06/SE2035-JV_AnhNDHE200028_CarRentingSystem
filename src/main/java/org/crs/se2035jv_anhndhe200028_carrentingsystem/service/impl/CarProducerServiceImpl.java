package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarProducerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarProducerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarProducerServiceImpl implements CarProducerService {

    private final CarProducerRepository carProducerRepository;

    @Override
    public void save(CarProducer carProducer) {
        if (carProducerRepository.findByProducerName(carProducer.getProducerName()) != null) {
            throw new DuplicateResourceException("Producer name already exists!");
        }
        carProducerRepository.save(carProducer);
    }

    @Override
    public void update(CarProducer carProducer) {
        CarProducer producer = carProducerRepository.findByProducerName(carProducer.getProducerName());
        if (producer != null && !producer.getProducerID().equals(carProducer.getProducerID())) {
            throw new DuplicateResourceException("Producer name already exists!");
        }
        carProducerRepository.save(carProducer);
    }

    @Override
    public CarProducer findByProducerName(String producerName) {
        return carProducerRepository.findByProducerName(producerName);
    }

    @Override
    public CarProducer findByProducerID(Integer id) {
        return carProducerRepository.findByProducerID(id);
    }

    @Override
    public List<CarProducer> getAll() {
        return carProducerRepository.findAll();
    }
}
