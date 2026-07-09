package org.crs.se2035jv_anhndhe200028_carrentingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerID;

    @Column(name = "FullName", columnDefinition = "NVARCHAR(200)", nullable = false)
    private String fullName;

    @Column(name = "Mobile", columnDefinition = "VARCHAR(15)", nullable = false)
    private String mobile;

    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "IdentityCard", columnDefinition = "VARCHAR(20)", nullable = false)
    private String identityCard;

    @Column(name = "LicenceNumber", columnDefinition = "VARCHAR(20)", nullable = false)
    private String licenceNumber;

    @Column(name = "LicenceDate", nullable = false)
    private LocalDate licenceDate;

    @OneToOne(optional = false)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;

}
