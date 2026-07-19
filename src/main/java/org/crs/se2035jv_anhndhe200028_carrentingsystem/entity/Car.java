package org.crs.se2035jv_anhndhe200028_carrentingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.CarStatus;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarID")
    private Integer carID;

    @Column(name = "CarName", columnDefinition = "NVARCHAR(200)", nullable = false)
    @NotBlank(message = "Car name is required!")
    @Size(max = 200, message = "Car name must not exceed 200 characters!")
    private String carName;

    @Column(name = "CarModelYear", nullable = false)
    @NotNull(message = "Car model year is required!")
    private Integer carModelYear;

    @Column(name = "Color", columnDefinition = "NVARCHAR(50)", nullable = false)
    @NotBlank(message = "Color is required!")
    @Size(max = 50, message = "Color must not exceed 50 characters!")
    private String color;

    @Column(name = "Capacity", nullable = false)
    @NotNull(message = "Capacity is required!")
    @Min(value = 1, message = "Capacity must be at least 1!")
    private Integer capacity;

    @Column(name = "Description", columnDefinition = "NVARCHAR(1000)", nullable = false)
    @NotBlank(message = "Description is required!")
    @Size(max = 1000, message = "Description must not exceed 1000 characters!")
    private String description;

    @Column(name = "ImportDate", nullable = false)
    @NotNull(message = "Import date is required!")
    @PastOrPresent(message = "Import date cannot be in the future!")
    private LocalDate importDate;

    @Column(name = "RentPrice", precision = 10, nullable = false)
    @NotNull(message = "Rent price is required!")
    @DecimalMin(value = "1", message = "Rent price must be greater than 0!")
    private BigDecimal rentPrice;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "Status", length = 10, nullable = false)
    @NotNull(message = "Status is required!")
    private CarStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ProducerID", nullable = false)
    @NotNull(message = "Producer is required!")
    private CarProducer producer;

}
