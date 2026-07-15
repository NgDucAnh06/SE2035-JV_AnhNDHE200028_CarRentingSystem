package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchReportDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
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
            carRental.setStatus("PENDING");
        }
        
        carRentalRepository.save(carRental);

        if (carRental.getCar() != null && carRental.getCar().getCarID() != null) {
            Car car = carRepository.findById(carRental.getCar().getCarID())
                    .orElseThrow(() -> new IllegalArgumentException("Car not found"));
            car.setStatus("RENTED");
            carRepository.save(car);
        }
    }

    @Override
    public List<CarRental> getAllCarRentalByCustomer(Customer customer) {
        List<CarRental> rentalList = carRentalRepository.getAllByCustomerOrderByCarRenIDDesc(customer);
        return rentalList;
    }

    @Override
    public void createRentals(Customer customer, List<Integer> carIds, LocalDate pickupDate, LocalDate returnDate) {
        if (carIds == null || carIds.isEmpty()) {
            return;
        }
        for (Integer carId : carIds) {
            Car car = carRepository.findById(carId).orElse(null);
            if (car != null && "AVAILABLE".equals(car.getStatus())) {
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
                        .status("PENDING")
                        .build();

                this.save(rental);
            }
        }
    }

    @Override
    public List<RentalSummaryDTO> showManagement() {
        return carRentalRepository.findCarRentalSummaries();
    }

    @Override
    public void updateStatus(Integer id, String status) {
        CarRental rental = carRentalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Rental not found"));
        rental.setStatus(status);
        carRentalRepository.save(rental);

        if (rental.getCar() != null && rental.getCar().getCarID() != null) {
            Car car = carRepository.findById(rental.getCar().getCarID()).orElse(null);
            if (car != null) {
                if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
                    car.setStatus("AVAILABLE");
                } else if ("ACTIVE".equals(status) || "RENTED".equals(status)) {
                    car.setStatus("RENTED");
                }
                carRepository.save(car);
            }
        }
    }

    @Override
    public List<RentalSummaryDTO> showReport(SearchReportDTO searchReportDTO) {
        return carRentalRepository.findCarRentalReport(searchReportDTO.getPickupDate(), searchReportDTO.getReturnDate(),
                searchReportDTO.getFullName());
    }
}
