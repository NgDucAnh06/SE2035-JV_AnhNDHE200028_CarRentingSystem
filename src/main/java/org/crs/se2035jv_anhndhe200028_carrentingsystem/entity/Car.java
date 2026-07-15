package org.crs.se2035jv_anhndhe200028_carrentingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private String carName;

    @Column(name = "CarModelYear", nullable = false)
    @NotNull(message = "Car model year is required!")
    private Integer carModelYear;

    @Column(name = "Color", columnDefinition = "NVARCHAR(50)", nullable = false)
    @NotBlank(message = "Color is required!")
    private String color;

    @Column(name = "Capacity", nullable = false)
    @NotNull(message = "Capacity is required!")
    private Integer capacity;

    @Column(name = "Description", columnDefinition = "NVARCHAR(1000)", nullable = false)
    @NotBlank(message = "Description is required!")
    private String description;

    @Column(name = "ImportDate", nullable = false)
    @NotNull(message = "Import date is required!")
    private LocalDate importDate;

    @Column(name = "RentPrice", precision = 10, nullable = false)
    @NotNull(message = "Rent price is required!")
    private BigDecimal rentPrice;

    @Column(name = "Status", columnDefinition = "NVARCHAR(10)", nullable = false)
    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ProducerID", nullable = false)
    private CarProducer producer;

}
