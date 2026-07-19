package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalReportStatsDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchHistoryRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchReportDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.CarStatus;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRentalRepository carRentalRepository;
    private final CarRepository carRepository;

    @Override
    public void save(CarRental carRental) {
        if (carRental.getStatus() == null) {
            carRental.setStatus(RentalStatus.ACTIVE);
        }
        
        carRentalRepository.save(carRental);

        if (carRental.getCar() != null && carRental.getCar().getCarID() != null) {
            Car car = carRepository.findById(carRental.getCar().getCarID())
                    .orElseThrow(() -> new IllegalArgumentException("Car not found"));
            car.setStatus(CarStatus.RENTED);
            carRepository.save(car);
        }
    }

    @Override
    public Page<CarRental> showHistoryByCustomer(Customer customer, Pageable pageable, SearchHistoryRequest searchHistoryRequest) {
        RentalStatus status = parseRentalStatus(searchHistoryRequest.getStatus());
        return carRentalRepository.findCarRentalHistory(
                customer.getCustomerID(),
                searchHistoryRequest.getCarName(),
                searchHistoryRequest.getFromDate(),
                searchHistoryRequest.getToDate(),
                status,
                pageable);
    }

    @Override
    public void createRentals(Customer customer, List<Integer> carIds, LocalDate pickupDate, LocalDate returnDate) {
        if (carIds == null || carIds.isEmpty()) {
            return;
        }
        for (Integer carId : carIds) {
            Car car = carRepository.findById(carId).orElse(null);
            if (car != null && car.getStatus() == CarStatus.AVAILABLE) {
                long days = ChronoUnit.DAYS.between(pickupDate, returnDate);
                if (days <= 0) {
                    days = 1;
                }
                BigDecimal totalRentalPrice = car.getRentPrice().multiply(BigDecimal.valueOf(days));
                CarRental rental = CarRental.builder()
                        .customer(customer)
                        .car(car)
                        .pickupDate(pickupDate)
                        .returnDate(returnDate)
                        .rentPrice(totalRentalPrice)
                        .status(RentalStatus.ACTIVE)
                        .build();
                car.setStatus(CarStatus.RENTED);
                carRepository.save(car);
                this.save(rental);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalSummaryDTO> showManagement(SearchHistoryRequest searchRequest, Pageable pageable) {
        String carName = normalizeFilter(searchRequest.getCarName());
        RentalStatus status = parseRentalStatus(searchRequest.getStatus());
        return carRentalRepository.findCarRentalManagement(
                carName,
                searchRequest.getFromDate(),
                searchRequest.getToDate(),
                status,
                pageable
        );
    }

    private String normalizeFilter(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private RentalStatus parseRentalStatus(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return RentalStatus.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public void updateStatus(Integer id, RentalStatus targetStatus) {
        CarRental rental = carRentalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Rental not found"));
        RentalStatus currentStatus = rental.getStatus();

        if (!isValidStatusTransition(currentStatus, targetStatus)) {
            throw new IllegalStateException(
                    "Cannot change rental status from " + currentStatus + " to " + targetStatus
            );
        }

        rental.setStatus(targetStatus);
        carRentalRepository.save(rental);

        if (rental.getCar() != null && rental.getCar().getCarID() != null) {
            Car car = carRepository.findById(rental.getCar().getCarID()).orElse(null);
            if (car != null) {
                if (targetStatus == RentalStatus.COMPLETED || targetStatus == RentalStatus.CANCELED) {
                    car.setStatus(CarStatus.AVAILABLE);
                } else {
                    car.setStatus(CarStatus.RENTED);
                }
                carRepository.save(car);
            }
        }
    }

    private boolean isValidStatusTransition(RentalStatus currentStatus, RentalStatus targetStatus) {
        return (currentStatus == RentalStatus.ACTIVE
                && (targetStatus == RentalStatus.RENTING || targetStatus == RentalStatus.CANCELED))
                || (currentStatus == RentalStatus.RENTING && targetStatus == RentalStatus.COMPLETED);
    }

    @Override
    public void cancelRentalByCustomer(Integer id, Customer customer) {
        CarRental rental = carRentalRepository.findById(id).orElse(null);
        if (rental == null || customer == null
                || !rental.getCustomer().getCustomerID().equals(customer.getCustomerID())) {
            throw new IllegalArgumentException("Rental not found or access denied.");
        }

        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new IllegalStateException("Only rentals waiting for pickup can be canceled.");
        }

        updateStatus(id, RentalStatus.CANCELED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalSummaryDTO> showReport(SearchReportDTO searchReportDTO, Pageable pageable) {
        return carRentalRepository.findCarRentalReport(
                searchReportDTO.getPickupDate(),
                searchReportDTO.getReturnDate(),
                normalizeFilter(searchReportDTO.getFullName()),
                normalizeFilter(searchReportDTO.getCarName()),
                parseRentalStatus(searchReportDTO.getStatus()),
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RentalReportStatsDTO getReportStats(SearchReportDTO searchReportDTO) {
        Object[] values = carRentalRepository.findCarRentalReportStats(
                searchReportDTO.getPickupDate(),
                searchReportDTO.getReturnDate(),
                normalizeFilter(searchReportDTO.getFullName()),
                normalizeFilter(searchReportDTO.getCarName()),
                parseRentalStatus(searchReportDTO.getStatus())
        ).orElse(new Object[0]);

        return new RentalReportStatsDTO(
                toLong(valueAt(values, 0)),
                toBigDecimal(valueAt(values, 1)),
                toLong(valueAt(values, 2)),
                toLong(valueAt(values, 3)),
                toLong(valueAt(values, 4))
        );
    }

    private Object valueAt(Object[] values, int index) {
        return index < values.length ? values[index] : null;
    }

    private long toLong(Object value) {
        return value instanceof Number number ? number.longValue() : 0L;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
    }

    @Override
    public Page<CarRental> getCompletedRentals(Customer customer, Pageable pageable) {
        return carRentalRepository.findByCustomerAndStatus(customer, RentalStatus.COMPLETED, pageable);
    }

    @Override
    public CarRental findById(Integer id) {
        return carRentalRepository.findById(id).orElse(null);
    }
}
