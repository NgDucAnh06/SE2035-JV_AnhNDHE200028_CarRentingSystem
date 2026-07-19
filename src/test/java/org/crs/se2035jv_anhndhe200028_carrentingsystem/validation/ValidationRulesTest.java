package org.crs.se2035jv_anhndhe200028_carrentingsystem.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchReportDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.CarStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationRulesTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void reportStartDateMustNotBeAfterEndDate() {
        SearchReportDTO request = new SearchReportDTO();
        request.setPickupDate(LocalDate.of(2026, 7, 20));
        request.setReturnDate(LocalDate.of(2026, 7, 19));

        Set<ConstraintViolation<SearchReportDTO>> violations = validator.validate(request);

        assertTrue(hasViolation(violations, "dateRangeValid"));
    }

    @Test
    void carMustRejectInvalidCapacity() {
        Car car = Car.builder()
                .carName("Test car")
                .carModelYear(2024)
                .color("Black")
                .capacity(0)
                .description("Test description")
                .importDate(LocalDate.now())
                .rentPrice(BigDecimal.ONE)
                .status(CarStatus.AVAILABLE)
                .producer(CarProducer.builder()
                        .producerName("Test producer")
                        .address("Test address")
                        .country("Vietnam")
                        .build())
                .build();

        Set<ConstraintViolation<Car>> violations = validator.validate(car);

        assertTrue(hasViolation(violations, "capacity"));
    }

    @Test
    void producerMustRejectBlankAddress() {
        CarProducer producer = CarProducer.builder()
                .producerName("Test producer")
                .address(" ")
                .country("Vietnam")
                .build();

        Set<ConstraintViolation<CarProducer>> violations = validator.validate(producer);

        assertTrue(hasViolation(violations, "address"));
    }

    private static boolean hasViolation(Set<? extends ConstraintViolation<?>> violations, String property) {
        return violations.stream()
                .anyMatch(violation -> property.equals(violation.getPropertyPath().toString()));
    }
}
