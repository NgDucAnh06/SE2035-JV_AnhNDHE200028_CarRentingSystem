package org.crs.se2035jv_anhndhe200028_carrentingsystem.config;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.CarStatus;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarProducerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CarProducerRepository carProducerRepository;
    private final CarRepository carRepository;
    private final CarRentalRepository carRentalRepository;

    @Override
    @Transactional
    public void run(String... args) {
        boolean newDatabase = accountRepository.count() == 0
                && carProducerRepository.count() == 0
                && carRepository.count() == 0;

        carRentalRepository.replaceStatus("PENDING", RentalStatus.RENTING.name());
        carRentalRepository.replaceStatus("ACTIVE", RentalStatus.WAITING_FOR_PICKUP.name());

        // Initialize admin account
        if (accountRepository.findByAccountName("admin").isEmpty()) {
            Account admin = Account.builder()
                    .accountName("admin")
                    .password("admin")
                    .email("admin@fucar.com")
                    .role("ADMIN")
                    .build();
            accountRepository.save(admin);
        }

        // Initialize test user account
        Account testUser = accountRepository.findByAccountName("test").orElse(null);
        if (testUser == null) {
            testUser = Account.builder()
                    .accountName("test")
                    .password("test@123")
                    .email("test@test.com")
                    .role("customer")
                    .build();

            Customer customer = Customer.builder()
                    .account(testUser)
                    .fullName("Test User")
                    .mobile("0123456789")
                    .identityCard("012345678912")
                    .licenceNumber("012345678912")
                    .licenceDate(LocalDate.now().minusYears(2))
                    .birthday(LocalDate.now().minusYears(20))
                    .build();

            testUser.setCustomer(customer);
            testUser = accountRepository.save(testUser);
        }

        if (testUser.getCustomer() == null) {
            Customer customer = createTestCustomer(testUser);
            testUser.setCustomer(customer);
            testUser = accountRepository.save(testUser);
        }

        List<Car> cars;
        if (newDatabase) {
            Map<String, CarProducer> producers = initializeProducers();
            cars = initializeCars(producers);
        } else {
            cars = carRepository.findAll();
        }
        initializeRentalHistory(testUser.getCustomer(), cars);
    }

    private Customer createTestCustomer(Account account) {
        return Customer.builder()
                .account(account)
                .fullName("Test User")
                .mobile("0123456789")
                .identityCard("012345678912")
                .licenceNumber("012345678912")
                .licenceDate(LocalDate.now().minusYears(2))
                .birthday(LocalDate.now().minusYears(20))
                .build();
    }

    private Map<String, CarProducer> initializeProducers() {
        List<ProducerSeed> seeds = List.of(
                new ProducerSeed("Honda", "Tokyo", "Japan"),
                new ProducerSeed("Toyota", "Toyota City", "Japan"),
                new ProducerSeed("Hyundai", "Seoul", "South Korea"),
                new ProducerSeed("Mercedes-Benz", "Stuttgart", "Germany"),
                new ProducerSeed("BMW", "Munich", "Germany"),
                new ProducerSeed("Ford", "Dearborn, Michigan", "United States")
        );

        Map<String, CarProducer> producers = new LinkedHashMap<>();
        for (ProducerSeed seed : seeds) {
            CarProducer producer = carProducerRepository.findByProducerName(seed.name()).orElse(null);
            if (producer == null) {
                producer = carProducerRepository.save(CarProducer.builder()
                        .producerName(seed.name())
                        .address(seed.address())
                        .country(seed.country())
                        .build());
            }
            producers.put(seed.name(), producer);
        }
        return producers;
    }

    private List<Car> initializeCars(Map<String, CarProducer> producers) {
        List<CarSeed> seeds = List.of(
                new CarSeed("Honda City", 2023, "White", 5, 750_000, "Honda"),
                new CarSeed("Honda Civic", 2022, "Black", 5, 1_100_000, "Honda"),
                new CarSeed("Honda CR-V", 2023, "Red", 7, 1_400_000, "Honda"),
                new CarSeed("Honda HR-V", 2022, "Gray", 5, 1_200_000, "Honda"),
                new CarSeed("Toyota Vios", 2023, "Silver", 5, 700_000, "Toyota"),
                new CarSeed("Toyota Camry", 2022, "Black", 5, 1_500_000, "Toyota"),
                new CarSeed("Toyota Corolla Cross", 2023, "White", 5, 1_300_000, "Toyota"),
                new CarSeed("Toyota Fortuner", 2022, "Brown", 7, 1_600_000, "Toyota"),
                new CarSeed("Hyundai Accent", 2023, "Blue", 5, 680_000, "Hyundai"),
                new CarSeed("Hyundai Elantra", 2022, "Red", 5, 950_000, "Hyundai"),
                new CarSeed("Hyundai Tucson", 2023, "Gray", 5, 1_350_000, "Hyundai"),
                new CarSeed("Hyundai Santa Fe", 2022, "Black", 7, 1_550_000, "Hyundai"),
                new CarSeed("Mercedes-Benz C200", 2022, "Black", 5, 2_500_000, "Mercedes-Benz"),
                new CarSeed("Mercedes-Benz E300", 2021, "White", 5, 3_200_000, "Mercedes-Benz"),
                new CarSeed("Mercedes-Benz GLC 300", 2023, "Blue", 5, 3_500_000, "Mercedes-Benz"),
                new CarSeed("BMW 320i", 2022, "White", 5, 2_600_000, "BMW"),
                new CarSeed("BMW 530i", 2021, "Black", 5, 3_300_000, "BMW"),
                new CarSeed("BMW X3", 2023, "Gray", 5, 3_600_000, "BMW"),
                new CarSeed("Ford Ranger", 2023, "Orange", 5, 1_400_000, "Ford"),
                new CarSeed("Ford Everest", 2022, "Black", 7, 1_700_000, "Ford")
        );

        List<Car> cars = new ArrayList<>();
        for (int index = 0; index < seeds.size(); index++) {
            CarSeed seed = seeds.get(index);
            Car car = carRepository.findByCarName(seed.name()).orElse(null);
            if (car == null) {
                car = carRepository.save(Car.builder()
                        .carName(seed.name())
                        .carModelYear(seed.modelYear())
                        .color(seed.color())
                        .capacity(seed.capacity())
                        .description("Well-maintained " + seed.name() + " with automatic transmission and air conditioning.")
                        .importDate(LocalDate.now().minusMonths(index + 2L))
                        .rentPrice(BigDecimal.valueOf(seed.dailyPrice()))
                        .status(CarStatus.AVAILABLE)
                        .producer(producers.get(seed.producerName()))
                        .build());
            }
            cars.add(car);
        }
        return cars;
    }

    private void initializeRentalHistory(Customer customer, List<Car> cars) {
        LocalDate today = LocalDate.now();
        int historySize = Math.min(10, cars.size());
        for (int index = 0; index < historySize; index++) {
            Car car = cars.get(index);
            if (carRentalRepository.existsByCustomerAndCarAndStatus(
                    customer, car, RentalStatus.COMPLETED)) {
                continue;
            }

            LocalDate pickupDate = today.minusDays(120L - index * 10L);
            LocalDate returnDate = pickupDate.plusDays(2L + index % 4L);
            long rentalDays = ChronoUnit.DAYS.between(pickupDate, returnDate);

            carRentalRepository.save(CarRental.builder()
                    .customer(customer)
                    .car(car)
                    .pickupDate(pickupDate)
                    .returnDate(returnDate)
                    .rentPrice(car.getRentPrice().multiply(BigDecimal.valueOf(rentalDays)))
                    .status(RentalStatus.COMPLETED)
                    .build());
        }
    }

    private record ProducerSeed(String name, String address, String country) {
    }

    private record CarSeed(
            String name,
            int modelYear,
            String color,
            int capacity,
            long dailyPrice,
            String producerName
    ) {
    }
}
