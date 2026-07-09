package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerByMobile(String mobile);
    Customer findCustomerByIdentityCard(String identity);
    Customer findCustomerByLicenceNumber(String licence);

}
