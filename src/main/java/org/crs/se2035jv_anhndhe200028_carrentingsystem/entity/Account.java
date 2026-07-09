package org.crs.se2035jv_anhndhe200028_carrentingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private Integer accountID;

    @Column(name = "AccountName", columnDefinition = "NVARCHAR(100)", nullable = false)
    private String accountName;

    @Column(name = "Email", columnDefinition = "VARCHAR(200)", nullable = false)
    private String email;

    @Column(name = "Password", columnDefinition = "VARCHAR(200)", nullable = false)
    private String password;

    @Column(name = "Role", columnDefinition = "NVARCHAR(10)", nullable = false)
    private String role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Customer customer;
}
