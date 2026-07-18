package org.crs.se2035jv_anhndhe200028_carrentingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CarRental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarRenID")
    private Integer carRenID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CarID", nullable = false)
    private Car car;

    @Column(name = "PickupDate", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "ReturnDate", nullable = false)
    private LocalDate returnDate;

    @Column(name = "RentPrice", precision = 10, nullable = false)
    private BigDecimal rentPrice;

    @Column(name = "Status", columnDefinition = "NVARCHAR(30)", nullable = false)
    private String status;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (pickupDate != null && returnDate != null && !pickupDate.isBefore(returnDate)) {
            throw new IllegalArgumentException("PickupDate must be before ReturnDate");
        }
    }

}
