package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus;

public interface CarRentalRepository extends JpaRepository<CarRental, Integer> {
    Page<CarRental> findAllByCustomer(Customer customer, Pageable pageable);

    List<CarRental> findAllByCustomer(Customer customer);

    List<CarRental> findAllByCar(Car car);

    List<CarRental> findAllByCarIn(List<Car> cars);

    Page<CarRental> findByCustomerAndStatus(Customer customer, RentalStatus status, Pageable pageable);

    boolean existsByCustomerAndCarAndStatus(Customer customer, Car car, RentalStatus status);

    @Modifying
    @Query(value = "UPDATE CarRental SET Status = :newStatus WHERE Status = :oldStatus", nativeQuery = true)
    int replaceStatus(@Param("oldStatus") String oldStatus, @Param("newStatus") String newStatus);

    @Query(value = "SELECT cr.carRenID AS carRenID, ct.fullName AS fullName, c.carName AS carName, " +
            "cr.pickupDate AS pickupDate, cr.returnDate AS returnDate, " +
            "cr.rentPrice AS rentPrice, cr.status AS status " +
            "FROM CarRental cr " +
            "JOIN cr.customer ct " +
            "JOIN cr.car c " +
            "WHERE (:carName IS NULL OR :carName = '' " +
            "       OR LOWER(c.carName) LIKE LOWER(CONCAT('%', :carName, '%'))) " +
            "AND (:fromDate IS NULL OR cr.pickupDate >= :fromDate) " +
            "AND (:toDate IS NULL OR cr.returnDate <= :toDate) " +
            "AND (:status IS NULL OR cr.status = :status) " +
            "ORDER BY cr.carRenID DESC",
            countQuery = "SELECT COUNT(cr) FROM CarRental cr " +
                    "JOIN cr.customer ct " +
                    "JOIN cr.car c " +
                    "WHERE (:carName IS NULL OR :carName = '' " +
                    "       OR LOWER(c.carName) LIKE LOWER(CONCAT('%', :carName, '%'))) " +
                    "AND (:fromDate IS NULL OR cr.pickupDate >= :fromDate) " +
                    "AND (:toDate IS NULL OR cr.returnDate <= :toDate) " +
                    "AND (:status IS NULL OR cr.status = :status)")
    Page<RentalSummaryDTO> findCarRentalManagement(@Param("carName") String carName,
                                                    @Param("fromDate") LocalDate fromDate,
                                                    @Param("toDate") LocalDate toDate,
                                                    @Param("status") RentalStatus status,
                                                    Pageable pageable);

    @Query(value = """
            SELECT cr.carRenID AS carRenID, ct.fullName AS fullName, c.carName AS carName,
                   cr.pickupDate AS pickupDate, cr.returnDate AS returnDate,
                   cr.rentPrice AS rentPrice, cr.status AS status
            FROM CarRental cr
            JOIN cr.customer ct
            JOIN cr.car c
            WHERE (:fullName IS NULL
                   OR LOWER(ct.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
              AND (:carName IS NULL
                   OR LOWER(c.carName) LIKE LOWER(CONCAT('%', :carName, '%')))
              AND (:pickupDate IS NULL OR cr.pickupDate >= :pickupDate)
              AND (:returnDate IS NULL OR cr.returnDate <= :returnDate)
              AND (:status IS NULL OR cr.status = :status)
            ORDER BY cr.carRenID DESC
            """,
            countQuery = """
            SELECT COUNT(cr)
            FROM CarRental cr
            JOIN cr.customer ct
            JOIN cr.car c
            WHERE (:fullName IS NULL
                   OR LOWER(ct.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
              AND (:carName IS NULL
                   OR LOWER(c.carName) LIKE LOWER(CONCAT('%', :carName, '%')))
              AND (:pickupDate IS NULL OR cr.pickupDate >= :pickupDate)
              AND (:returnDate IS NULL OR cr.returnDate <= :returnDate)
              AND (:status IS NULL OR cr.status = :status)
            """)
    Page<RentalSummaryDTO> findCarRentalReport(@Param("pickupDate") LocalDate pickupDate,
                                               @Param("returnDate") LocalDate returnDate,
                                               @Param("fullName") String fullName,
                                               @Param("carName") String carName,
                                               @Param("status") RentalStatus status,
                                               Pageable pageable);

    @Query("""
            SELECT COUNT(cr),
                   COALESCE(SUM(CASE WHEN cr.status = 'COMPLETED' THEN cr.rentPrice ELSE 0 END), 0),
                   COALESCE(SUM(CASE WHEN cr.status = 'COMPLETED' THEN 1 ELSE 0 END), 0),
                   COALESCE(SUM(CASE WHEN cr.status IN ('ACTIVE', 'RENTING') THEN 1 ELSE 0 END), 0),
                   COALESCE(SUM(CASE WHEN cr.status = 'CANCELED' THEN 1 ELSE 0 END), 0)
            FROM CarRental cr
            JOIN cr.customer ct
            JOIN cr.car c
            WHERE (:fullName IS NULL
                   OR LOWER(ct.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
              AND (:carName IS NULL
                   OR LOWER(c.carName) LIKE LOWER(CONCAT('%', :carName, '%')))
              AND (:pickupDate IS NULL OR cr.pickupDate >= :pickupDate)
              AND (:returnDate IS NULL OR cr.returnDate <= :returnDate)
              AND (:status IS NULL OR cr.status = :status)
            """)
    Optional<Object[]> findCarRentalReportStats(@Param("pickupDate") LocalDate pickupDate,
                                            @Param("returnDate") LocalDate returnDate,
                                            @Param("fullName") String fullName,
                                            @Param("carName") String carName,
                                            @Param("status") RentalStatus status);

    @Query("SELECT cr FROM CarRental cr WHERE cr.customer.customerID = :customerId " +
           "AND (:carName IS NULL OR cr.car.carName LIKE %:carName%) " +
           "AND (:pickupDate IS NULL OR cr.pickupDate >= :pickupDate) " +
           "AND (:returnDate IS NULL OR cr.returnDate <= :returnDate) " +
           "AND (:status IS NULL OR cr.status = :status)")
    Page<CarRental> findCarRentalHistory(@Param("customerId") Integer customerId,
                                         @Param("carName") String carName,
                                         @Param("pickupDate") LocalDate pickupDate,
                                         @Param("returnDate") LocalDate returnDate,
                                         @Param("status") RentalStatus status,
                                         Pageable pageable);
}
